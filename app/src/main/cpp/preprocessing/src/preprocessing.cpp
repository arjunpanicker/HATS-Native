#include <iostream>
#include <string>
#include <vector>
#include <utility>
#include <algorithm>
#include <regex>

#include "pre_utils.h"
#include "preprocessing.h"
#include "config.h"
#include "datasethandler.h"

namespace hats {
	std::size_t Preprocessing::findSubstringPosition(std::string value, std::string substr)
	{
		std::size_t foundPos = value.find(substr);

		// If substring lies in middle of the string
		if (foundPos != 0 && ((foundPos + substr.length()) != value.length()) && foundPos != std::string::npos) {
			foundPos = value.find(" " + substr + " ");

			foundPos = foundPos != std::string::npos ? foundPos + 1 : foundPos;
		}
		// If the substring is found in the start of the string
		if (foundPos == 0 && ((foundPos + substr.length()) != value.length())) {
			foundPos = value.find(substr + " ");
		}
		// If the substring is found in end of the string
		if (foundPos != 0 && ((foundPos + substr.length()) == value.length()) && foundPos != std::string::npos) {
			foundPos = value.find(" " + substr);

			foundPos = foundPos != std::string::npos ? foundPos + 1 : foundPos;
		}

		return foundPos;
	}

	StringList Preprocessing::toLowercase(StringList values)
	{
		StringList newList{};

		for (int i = 0; i < values.size(); i++) {
			std::string str = values.at(i);

			// Credit: https://stackoverflow.com/questions/313970/how-to-convert-stdstring-to-lower-case
			std::transform(str.begin(), str.end(), str.begin(), [](unsigned char c) { return std::tolower(c); });
			newList.push_back(str);
		}

		return newList;
	}

	std::string Preprocessing::toLowercase(std::string value)
	{
		// Credit: https://stackoverflow.com/questions/313970/how-to-convert-stdstring-to-lower-case
		std::transform(value.begin(), value.end(), value.begin(), [](unsigned char c) { return std::tolower(c); });
		return value;
	}

	StringList Preprocessing::removePunctuation(StringList values)
	{
		StringList newValues{};
		for (int i = 0; i < values.size(); i++) {
			newValues.push_back(removePunctuation(values.at(i)));
		}

		return newValues;
	}

	std::string Preprocessing::removePunctuation(std::string value)
	{
		for (int i = 0; i < value.size(); i++) {
			if (std::ispunct(value[i])) {
				value.erase(i--, 1);
			}
		}
		return value;
	}

	StringList Preprocessing::removeStopWords(StringList values, StringList stopWords)
	{
		StringList newValues{};
		StringList stopwordsList = stopWords;
		
		if (!stopWords.size()) {
			std::string stopwordFilename = files::STOPWORDS_FILENAME;
			TextHandler textHandler(stopwordFilename);
			stopwordsList = textHandler.read_txt();
		}


		for (int i = 0; i < values.size(); i++) {
			std::string value = values.at(i);
			std::string stopwordRemovedValue = removeStopWords(value, stopwordsList);

			newValues.push_back(stopwordRemovedValue);
		}

		return newValues;
	}

	std::string Preprocessing::removeStopWords(std::string value, StringList stopWords)
	{
		StringList stopwordsList = stopWords;

		if (!stopWords.size()) {
			std::string stopwordFilename = files::STOPWORDS_FILENAME;
			TextHandler textHandler(stopwordFilename);
			stopwordsList = textHandler.read_txt();
		}

		for (int i = 0; i < stopwordsList.size(); i++) {
			std::size_t foundPos = value.find(stopwordsList.at(i));

			while (!(foundPos == std::string::npos)) {
				value.erase(foundPos, stopwordsList.at(i).length());

				foundPos = value.find(stopwordsList.at(i));
			}
		}

		return value;
	}

	StringList Preprocessing::convertShortText(StringList values, DataTable shorttextData)
	{
		StringList newList{};
		DataTable shorttextInfo = shorttextData;

		if (!shorttextData.size()) {
			std::string filename = files::SMS_TRANSLATION_FILENAME;
			CSVHandler csvHandler(filename);
			shorttextInfo = csvHandler.read_csv();
		}

		for (int i = 0; i < values.size(); i++) {
			std::string convertedVal = convertShortText(values.at(i), shorttextData);

			newList.push_back(convertedVal);
		}

		return newList;
	}

	std::string Preprocessing::convertShortText(std::string value, DataTable shorttextData)
	{
		DataTable shorttextInfo = shorttextData;

		if (!shorttextData.size()) {
			std::string filename = files::SMS_TRANSLATION_FILENAME;
			CSVHandler csvHandler(filename);
			shorttextInfo = csvHandler.read_csv();
		}

		for (int col = 0; col < shorttextInfo.size(); col++) {
			std::string lemmaWord = shorttextInfo.at(col).first;
			StringList colValues = shorttextInfo.at(col).second;

			for (int i = 0; i < colValues.size(); i++) {
				std::size_t foundPos = findSubstringPosition(value, colValues.at(i));

				while (!(foundPos == std::string::npos)) {
					value.replace(foundPos, colValues.at(i).length(), lemmaWord);

					foundPos = findSubstringPosition(value, colValues.at(i));
				}/*

				std::regex reg(colValues.at(i));

				value = std::regex_replace(value, reg, lemmaWord);*/
			}
		}

		return value;
	}

	DataTable Preprocessing::pipeline(DataTable data, StringList stopWords, DataTable shortTextData)
	{
		DataTable preprocessedData;
		
		for (auto col = data.begin(); col != data.end(); col++) {
			std::string colName = (*col).first;
			StringList colData = (*col).second;

			// Convert each column to lowercase
			colData = toLowercase(colData);

			// Remove punctuations
			colData = removePunctuation(colData);

			// Remove stopwords
			colData = removeStopWords(colData, stopWords);

			// Convert short-texts
			colData = convertShortText(colData, shortTextData);

			preprocessedData.push_back({ colName, colData });
		}

		return preprocessedData;
	}

	std::string Preprocessing::pipeline(std::string sentence, StringList stopWords, DataTable shortTextData)
	{
		std::string preprocessedSentence;

		// Convert sentence to lowercase
		preprocessedSentence = toLowercase(sentence);

		// Remove punctuations
		preprocessedSentence = removePunctuation(preprocessedSentence);

		// Remove stopwords
		preprocessedSentence = removeStopWords(preprocessedSentence, stopWords);

		// Convert short-texts
		preprocessedSentence = convertShortText(preprocessedSentence, shortTextData);

		return preprocessedSentence;
	}
}
