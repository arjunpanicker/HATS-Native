#include <vector>
#include <algorithm>
#include <iostream>

#include "pre_utils.h"
#include "ml_utils.h"

namespace hats {

    DtOneHotVector argmaxOneHot(const std::vector<double> &data) {
        DtOneHotVector ohv(data.size(), 0);

        int32_t maxElementIndex = std::max_element(data.begin(), data.end()) - data.begin();
        ohv[maxElementIndex] = 1;

        return ohv;
    }

    std::string oneHotToVal(MapStoVi &ohvMap, const DtOneHotVector &ohv) {
        std::string label{""};
        for (MapStoVi::iterator ele = ohvMap.begin(); ele != ohvMap.end(); ++ele) {
            if (ele->second == ohv) {
                label = ele->first;
                std::cout << "Found match\n\n";
                break;
            }
        }

        return label;
    }
    
} // namespace hats
