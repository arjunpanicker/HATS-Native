#pragma once
#ifndef PREPROCESSING_H
#define PREPROCESSING_H

namespace hats {

	class Preprocessing {
	private:
		std::size_t findSubstringPosition(std::string value, std::string substr);
	public:
		Preprocessing() {}

		~Preprocessing() {}

		/// This method converts the provided list of string values into lowercase
		StringList toLowercase(StringList values);

		/// Converts a string to its lowercase.
		std::string toLowercase(std::string value);

		/// This method removes punctuations from the list of string values
		StringList removePunctuation(StringList values);

		/// Removes punctuations from a string value
		std::string removePunctuation(std::string value);

		/// This method removes the stopwords present in each string in the given
		/// list
		StringList removeStopWords(StringList values, StringList stopWords = {});

		/// This method removes the stopwords present in the string
		std::string removeStopWords(std::string value, StringList stopWords = {});

		/// This method converts the short texts present in the provided list of strings
		/// according to the ST-MRD provided.
		StringList convertShortText(StringList values, DataTable shorttextData = {});

		/// This method converts the short texts present in the string according
		/// to the ST-MRD provided.
		std::string convertShortText(std::string value, DataTable shorttextData = {});

		// Preprocessing Pipeline
		DataTable pipeline(DataTable data, StringList stopWords = {}, DataTable shortTextData = {});
		
		// Preprocessing Pipeline
		std::string pipeline(std::string sentence, StringList stopWords = {}, DataTable shortTextData = {});
	};
}

#endif // !PREPROCESSING_H
