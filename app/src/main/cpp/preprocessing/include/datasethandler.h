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
			TextHandler(const std::string &fName) { filename = fName; }

			~TextHandler() {}

			// This method reads the contents of the text file and returns the contents as a vector
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

			// This method reads the contents of the csv file.
			DataTable read_csv();

			// This method writes the provided data into a csv file.
			std::string write_csv(const DataTable &data, std::string filename);

			// This method returns the shape of the csv file
			std::pair<int, int> shape() {
				if (_shape != std::pair<int, int>{0, _colCount}) {
					return _shape;
				}
				else {
					_result = read_csv();
					return _shape;
				}
			}

			void shape(const int &rows, const int &cols) { _shape = std::make_pair(rows, cols); }
	};

	class MapHandler {
		public:
			static void saveMap(const std::string &filename, MapStoVi &data);

			static MapStoVi loadMap(const std::string &filename);
	};
}

#endif // !DATASET_HANDLER_H