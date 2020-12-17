# pragma once
#ifndef ML_H
#define ML_H

#include <cstdlib>

namespace hats {

    class Connection {
        private:
            double weight;
            double deltaWeight;
            static double randomWeight(void) { return rand() / double(RAND_MAX); }

        public:
            Connection();
            void setWeight(double val) { weight = val; }
            double getWeight() const { return weight; }
            void setDeltaWeight(double val) { deltaWeight = val; }
            double getDeltaWeight() const { return deltaWeight; }

    };

    class Neuron {
        private:
            double lr = 0.15;     // [0.0...1.0] overall net training rate
            double alpha = 0.5;  // [0.0...n] multiplier of last weight change (momentum)
            static double transferFunction(double x);
            static double transferFunctionDerivative(double x);
            double m_outputVal;
            double m_gradient;
            int32_t m_myIndex;
            std::vector<Connection> m_outputWeights;
            double sumDOW(const Layer &nextLayer) const;

        public:
            Neuron(int32_t numOutputs, int32_t myIndex);
            void setOutputVal(double val) { m_outputVal = val; }
            double getOutputVal() const { return m_outputVal; }
            void feedForward(const Layer &prevLayer);
            void calcOutputGradients(double targetVal);
            void calcHiddenGradients(const Layer &nextLayer);
            void updateInputWeights(Layer &prevLayer);
    };

    class Net {
        private:
            std::vector<Layer> m_layers;  // m_layers[layerNum][neuronNum]
            double m_error;
            double m_recentAverageError;
            double m_recentAverageSmoothingFactor = 100.0;

        public:
            Net(const std::vector<unsigned> &topology);
            void feedForward(const std::vector<double> &inputVals);
            void backProp(const std::vector<double> &targetVals);
            void getResults(std::vector<double> &resultVals) const;
    };

}

#endif // !ML_H