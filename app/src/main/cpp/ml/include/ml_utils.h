#pragma once
#ifndef ML_UTILS_H
#define ML_UTILS_H

namespace hats {

    // Neuron-Neuron connection
    class Connection;

    class Neuron;

    typedef std::vector<Neuron> Layer;

    template <typename T>
    MapStoVi oneHotEncode(const std::vector<T> &data) {
        MapStoVi labelMap;
        for (int i = 0; i < data.size(); i++) {
            DtOneHotVector ohv(data.size(), 0);
            ohv[i] = 1;
            
            labelMap.insert({data[i], ohv});
        }

        return labelMap;
    }

    DtOneHotVector argmaxOneHot(const std::vector<double> &data);

    std::string oneHotToVal(MapStoVi &ohvMap, const DtOneHotVector &ohv);
}


#endif // !ML_UTILS_H