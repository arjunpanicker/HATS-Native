#pragma once
#ifndef CONFIG_H
#define CONFIG_H
#include <string>

namespace hats {
	// Base configurations
	namespace base {
		const std::string LANGUAGE = "en";
	}

	// The location must be relative to the location of the out file (while testing)
	namespace files {
		const std::string SOURCE_DATASET_FILENAME = "home_auto_dataset.csv";
		const std::string SMS_TRANSLATION_FILENAME = "/sms_translations.csv";
		const std::string MULTILINGUAL_TRANSLATION_FILENAME = "/translations_data.csv";
		const std::string STOPWORDS_FILENAME = "stop_words.txt";

		
		const std::string NEW_DATASET_FILENAME = "/hats_data.csv";

		const std::string MAP_FILE = "label_ohv.map";
	}

	// Error strings
	namespace errors {
		// File Errors
		const std::string FILE_OPEN_ERROR = "Could not open files";
	}
}

#endif // !CONFIG_H

