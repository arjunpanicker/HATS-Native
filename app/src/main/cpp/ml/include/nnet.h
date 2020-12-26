# pragma once
#ifndef ML_H
#define ML_H

#include <cstdlib>

namespace hats {

    enum NeuronType { INPUT, HIDDEN, OUTPUT };
    enum TransferFunctionType { TANH, SOFTMAX };

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
            NeuronType neuronType;
            double m_gradient;
            int32_t m_myIndex;
            std::vector<Connection> m_outputWeights;
            double sumDOW(const Layer &nextLayer) const;

        public:
            Neuron(int32_t numOutputs, int32_t myIndex, NeuronType nType);
            void setOutputVal(double val) { m_outputVal = val; }
            double getOutputVal() const { return m_outputVal; }
            void setNeuronType(NeuronType ntype) { neuronType = ntype; }
            NeuronType getNeuronType() { return neuronType; }
            void feedForward(const Layer &prevLayer, const bool &isOutputLayer);
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
            static void ouputLayerSoftmaxFunction(Layer &outputLayer);

        public:
            Net(const std::vector<int> &topology);
            void feedForward(const hats::FasttextVector &inputVals);
            void backProp(const std::vector<int> &targetVals);
            void getResults(std::vector<double> &resultVals) const;
            void getWeights(std::vector<double> &weightVals) const;
    };

}

#endif // !ML_H