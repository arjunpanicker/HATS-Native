#pragma once
#ifndef PRE_UTILS_H
#define PRE_UTILS_H

#include <vector>
#include <string>
#include <utility>

namespace hats {

    typedef std::vector<std::string> StringList;
	typedef std::pair<std::string, std::vector<std::string>> DataColumn;
	typedef std::vector<DataColumn> DataTable;
    typedef std::vector<float> FloatList;
    
}

#endif // !PRE_UTILS_H