// Credit: https://www.gormanalysis.com/blog/reading-and-writing-csv-files-with-cpp/

#include <fstream>
#include <iostream>
#include <string>
#include <vector>
#include <utility>
#include <stdexcept>
#include <sstream>

#include "pre_utils.h"
#include "datasethandler.h"
#include "config.h"

namespace hats
{
	// TODO: Need to delegate this functionality to Java
	std::vector<DataColumn> CSVHandler::read_csv()
	{
		// Filename
		std::ifstream myFile{filename};

		if (!myFile.is_open())
		{
			std::cout << hats::errors::FILE_OPEN_ERROR;
			return {};
		}

		// Helper variables
		std::string line, colName, val;

		// Read the column names
		if (myFile.good())
		{
			std::getline(myFile, line);

			std::stringstream ss(line);

			while (std::getline(ss, colName, ','))
			{
				_result.push_back({colName, StringList{}});
				_colCount++;
			}
		}

		// Read data, line by line
		while (std::getline(myFile, line))
		{
			std::stringstream ss(line);
			int colIndex{0};

			size_t pos{0};

			while ((pos = line.find(",")) != std::string::npos)
			{
				val = line.substr(0, pos);
				line.erase(0, pos + 1);

				if (val.length())
				{
					_result.at(colIndex++).second.push_back(val);
				}
				else
				{
					colIndex++;
				}
			}

			if (line.length())
			{
				_result.at(colIndex).second.push_back(line);
			}

			_rowCount++;
		}

		myFile.close();

		// Update the shape of the result Table
		shape(_rowCount, _colCount);

		return _result;
	}

	// TODO: Need to delegate this functionality to Java
	std::string CSVHandler::write_csv(DataTable data, std::string filename)
	{
		// First, check if file already exists or not.
		// std::string addr{".../doc/dataset/"};
		// filename = addr + filename;
		std::string ext = ".csv";
		
		std::ifstream inFile{filename + ext};
		int count = 0;

		// Check this loop.
		while (inFile.good())
		{	
			std::cout << filename << " exists";
			inFile.close();
			
			filename += ++count;
			std::cout << filename;
			// filename = addr + filename;
			inFile.open(filename + ext);
		}
		inFile.close();

		// Create a new file with filename
		std::ofstream myFile{filename + ext};

		// Insert the column names or headers
		// for (int i = 0; i < data.size(); i++)
		// {
		// 	if (!data.at(i).first.empty())
		// 	{
		// 		myFile << data.at(i).first;

		// 		// No comma at the end of the line
		// 		if (i != data.size() - 1)
		// 		{
		// 			myFile << ",";
		// 		}
		// 	}
		// }
		// myFile << "\n";

		// Insert the column data
		for (int i = 0; i < data.at(0).second.size(); i++)
		{
			for (int j = 0; j < data.size(); j++)
			{
				myFile << data.at(j).second.at(i);

				// No comma at the end of the line
				if (j != data.size() - 1)
				{
					myFile << ",";
				}
			}
			myFile << "\n";
		}

		myFile.close();

		return filename + ext;
	}

	StringList TextHandler::read_txt()
	{
		// Filename
		std::ifstream myFile{filename};
		if (!myFile.is_open())
			return {};

		StringList content{};
		std::string line;

		if (myFile.good())
		{
			while (std::getline(myFile, line))
			{
				content.push_back(line);
			}
		}

		return content;
	}
} // namespace hats
