#pragma once
#ifndef DATASET_HANDLER_H
#define DATASET_HANDLER_H


namespace hats {

	class TextHandler {
	private:
		// Name of the text file
		std::string filename;

	public:
		TextHandler() {}
		TextHandler(std::string fName) { filename = fName; }

		~TextHandler() {}

		/// <summary>
		/// This method reads the contents of the text file and returns the contents as a vector
		/// </summary>
		/// <returns>Vector of strings</returns>
		StringList read_txt();
	};

	class CSVHandler {
	private:
		// Stores the result of the csv read
		DataTable _result;

		// Stores the number of rows and columns in the csv file.
		// The number of columns have been defaulted to 2 for our usecase.
		int _rowCount{ 0 }, _colCount{ 0 };

		// Stores the shape of the csv file
		std::pair<int, int> _shape{ _rowCount, _colCount };

		// Name of the csv file
		std::string filename;

	public:
		CSVHandler() {}
		CSVHandler(std::string fName) { filename = fName; }

		~CSVHandler() {}

		/// <summary>
		/// This method reads the contents of the csv file.
		/// </summary>
		/// <returns></returns>
		DataTable read_csv();

		/// <summary>
		/// This method writes the provided data into a csv file.
		/// </summary>
		/// <params name="data"> The data to be entered into the file </params>
		std::string write_csv(DataTable data, std::string filename);

		/// <summary>
		/// This method returns the shape of the csv file
		/// </summary>
		/// <returns></returns>
		std::pair<int, int> shape() {
			if (_shape != std::pair<int, int>{0, _colCount}) {
				return _shape;
			}
			else {
				_result = read_csv();
				return _shape;
			}
		}

		void shape(int rows, int cols) { _shape = std::make_pair(rows, cols); }
	};
}

#endif // !DATASET_HANDLER_H