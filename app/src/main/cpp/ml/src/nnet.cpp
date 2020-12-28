#include <iostream>
#include <vector>
#include <math.h>

#include "pre_utils.h"
#include "ml_utils.h"
#include "nnet.h"

namespace hats {

    // *********************** class Connection ****************************
    
    Connection::Connection() {
        weight = Connection::randomWeight();
    }

    // *********************** class Neuron ****************************
    
    Neuron::Neuron(int32_t numOutputs, int32_t myIndex, NeuronType nType) {
        for (int32_t c = 0; c < numOutputs; ++c) {
            m_outputWeights.push_back(Connection());
        }

        m_myIndex = myIndex;
        neuronType = nType;
    }

    void Neuron::feedForward(const Layer &prevLayer, const bool &isOutputLayer) {
        double sum = 0.0;

        // Sum the prev layer's outputs (which are our inputs)
        // Include the bias node from the previous layer

        for (int32_t n = 0; n < prevLayer.size(); ++n) {
            sum += prevLayer[n].m_outputVal * 
                    prevLayer[n].m_outputWeights[m_myIndex].getWeight();

            m_outputVal = isOutputLayer ? sum : Neuron::transferFunction(sum);
        }
    }

    double Neuron::transferFunction(double x) {
        // tanh - output range [-1, +1]

        // if (getNeuronType() != OUTPUT) return tanh(x);

        // return exp(x) / ();

        return tanh(x);
    }

    double Neuron::transferFunctionDerivative(double x) {
        // tanh derivative - output range [-1, +1]
        return 1 - x * x;
    }

    void Neuron::calcOutputGradients(double targetVal) {
        double delta = targetVal - m_outputVal;
        m_gradient = delta * Neuron::transferFunctionDerivative(m_outputVal);
    }

    void Neuron::calcHiddenGradients(const Layer &nextLayer) {
        double dow = sumDOW(nextLayer);
        m_gradient = dow * Neuron::transferFunctionDerivative(m_outputVal);
    }

    double Neuron::sumDOW(const Layer &nextLayer) const {
        double sum = 0.0;

        // Sum our contributions of the errors at the nodes we feed

        for (int32_t n = 0; n < nextLayer.size() - 1; ++n) {
            sum += m_outputWeights[n].getWeight() * nextLayer[n].m_gradient;
        }

        return sum;
    }

    void Neuron::updateInputWeights(Layer &prevLayer) {
        for (int32_t n = 0; n < prevLayer.size(); ++n) {
            Neuron &neuron = prevLayer[n];
            double oldDeltaWeight = neuron.m_outputWeights[m_myIndex].getDeltaWeight();

            double newDeltaWeight = 
                    // Individual input, magnified by the gradient and learning rate
                    lr
                    * neuron.getOutputVal()
                    * m_gradient
                    // Also add momentum = a fraction of the previous delta weight
                    + alpha
                    * oldDeltaWeight;
            
            neuron.m_outputWeights[m_myIndex].setDeltaWeight(newDeltaWeight);
            neuron.m_outputWeights[m_myIndex].setWeight(
                neuron.m_outputWeights[m_myIndex].getWeight() + newDeltaWeight
            );
        }
    }

    // *********************** class Net ****************************

    Net::Net(const std::vector<int> &topology) {
        int32_t numLayers = topology.size();

        for(int32_t layerNum = 0; layerNum < numLayers; ++layerNum) {
            m_layers.push_back(Layer());
            int32_t numOutputs = layerNum == numLayers - 1 ? 0 : topology[layerNum + 1];
            
            NeuronType nType;
            if (layerNum == 0) {
                nType = INPUT;
            } else if (layerNum == numLayers - 1) {
                nType = OUTPUT;
            } else {
                nType = HIDDEN;
            } 

            // We have made a new Layer. Now, we need to add neurons to the layer.
            // We also need to add the bias neuron to each layer
            for (int32_t neuronNum = 0; neuronNum <= topology[layerNum]; ++neuronNum) {
                m_layers.back().push_back(Neuron(numOutputs, neuronNum, nType));
            }

            // Force the bias node's output value to 1.0. It's the last neuron created above
            m_layers.back().back().setOutputVal(1.0);
        }
    }

    void Net::feedForward(const hats::FasttextVector &inputVals) {
        if (inputVals.size() != m_layers[0].size() - 1) return;

        // Assign the input values to the input neurons
        for (int32_t i = 0; i < inputVals.size(); ++i) {
            m_layers[0][i].setOutputVal(inputVals[i]);
        }

        // Forward Propagation
        for (int32_t layerNum = 1; layerNum < m_layers.size(); ++layerNum) {
            Layer &prevLayer = m_layers[layerNum - 1];

            for (int32_t n = 0; n < m_layers[layerNum].size() - 1; ++n) {
                m_layers[layerNum][n].feedForward(prevLayer, (layerNum == m_layers.size() - 1));
            }
        }

        Net::ouputLayerSoftmaxFunction(m_layers.back());
    }

    void Net::ouputLayerSoftmaxFunction(Layer &outputLayer) {
        std::vector<double> expValues;
        double sumExp = 0.0;

        for (int32_t n = 0; n < outputLayer.size() - 1; ++n) {
            double expVal = exp(outputLayer[n].getOutputVal());
            sumExp += expVal;
            expValues.push_back(expVal);
        }

        for (int32_t n = 0; n < outputLayer.size() - 1; ++n) {
            outputLayer[n].setOutputVal(expValues[n] / sumExp);
        }
    }

    void Net::backProp(const std::vector<int> &targetVals) {
        if (targetVals.size() != m_layers.back().size() - 1) return;

        // Calculate overall net error (RMS of output errors)
        Layer &outputLayer = m_layers.back();
        m_error = 0.0;

        for (int32_t n = 0; n < outputLayer.size() - 1; ++n) {
            double delta = targetVals[n] - outputLayer[n].getOutputVal();
            m_error += delta * delta;
        }

        m_error /= outputLayer.size() - 1;  // get avg of error squared
        m_error = sqrt(m_error);  // RMS


        // Implement a recent average measurement
        m_recentAverageError = 
                (m_recentAverageError * m_recentAverageSmoothingFactor + m_error)
                / (m_recentAverageSmoothingFactor + 1.0);

        // Calculate output layer gradients

        for (int32_t n = 0; n < outputLayer.size() - 1; ++n) {
            outputLayer[n].calcOutputGradients(targetVals[n]);
        }

        // Calculate gradients on hidden layers
        for (int32_t layerNum = m_layers.size() - 2; layerNum > 0; --layerNum) {
            Layer &hiddenLayer = m_layers[layerNum];
            Layer &nextLayer = m_layers[layerNum + 1];

            for (int32_t n = 0; n < hiddenLayer.size(); ++n) {
                hiddenLayer[n].calcHiddenGradients(nextLayer);
            }
        }

        // For all layers from outputs to first hidden layer,
        // update the connection weights

        for (int32_t layerNum = m_layers.size() - 1; layerNum > 0; --layerNum) {
            Layer &layer = m_layers[layerNum];
            Layer &prevLayer = m_layers[layerNum - 1];

            for (int32_t n = 0; n < layer.size() - 1; ++n) {
                layer[n].updateInputWeights(prevLayer);
            }
        }
    }

    void Net::getResults(std::vector<double> &resultVals) const {
        resultVals.clear();

        for (int32_t n = 0; n < m_layers.back().size() - 1; ++n) {
            resultVals.push_back(m_layers.back()[n].getOutputVal());
        }
    }

    void Net::getWeights(std::vector<double> &weightVals) const {
        weightVals.clear();

        // Iterate layers
        for (int32_t l = 0; l < m_layers.size(); ++l) {
            // Iterate each neuron and get weights;
            for (int32_t n = 0; n < m_layers[l].size(); ++n) {
                // TODO: Complete the code to get the connection weights
            }

        }
    }

}