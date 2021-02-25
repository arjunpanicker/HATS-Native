package com.example.hatsnative.helpers.services.ml;

import com.example.hatsnative.models.INet;

import java.util.Vector;

public class Net implements INet {
    Vector<Vector<Neuron>> m_layers;  // m_layers[layerNum][neuronNum]
    double m_error;
    double m_recentAverageError;
    double m_recentAverageSmoothingFactor = 100.0;

    @Override
    public void feedForward(Vector<Double> inputVals) {
        if (inputVals.size() != m_layers.get(0).size() - 1) return;

        // Assign the input values to the input neurons
        for (int i = 0; i < inputVals.size(); ++i) {
            m_layers.get(0).get(i).setOutputVal(inputVals.get(i));
        }

        // Forward Propagation
        for (int layerNum = 1; layerNum < m_layers.size(); ++layerNum) {
            Vector<Neuron> prevLayer = m_layers.get(layerNum - 1);

            for (int n = 0; n < m_layers.get(layerNum).size() - 1; ++n) {
                m_layers.get(layerNum).get(n).feedForward(prevLayer, (layerNum == m_layers.size() - 1));
            }
        }

        INet.ouputLayerSoftmaxFunction(m_layers.get(m_layers.size() - 1));
    }

    @Override
    public void backProp(Vector<Integer> targetVals) {
        if (targetVals.size() != m_layers.get(m_layers.size() - 1).size() - 1) return;

        // Calculate overall net error (RMS of output errors)
        Vector<Neuron> outputLayer = m_layers.get(m_layers.size() - 1);
        m_error = 0.0;

        for (int n = 0; n < outputLayer.size() - 1; ++n) {
            double delta = targetVals.get(n) - outputLayer.get(n).getOutputVal();
            m_error += delta * delta;
        }

        m_error /= outputLayer.size() - 1;  // get avg of error squared
        m_error = Math.sqrt(m_error);  // RMS


        // Implement a recent average measurement
        m_recentAverageError =
                (m_recentAverageError * m_recentAverageSmoothingFactor + m_error)
                        / (m_recentAverageSmoothingFactor + 1.0);

        // Calculate output layer gradients

        for (int n = 0; n < outputLayer.size() - 1; ++n) {
            outputLayer.get(n).calcOutputGradients(targetVals.get(n));
        }

        // Calculate gradients on hidden layers
        for (int layerNum = m_layers.size() - 2; layerNum > 0; --layerNum) {
            Vector<Neuron> hiddenLayer = m_layers.get(layerNum);
            Vector<Neuron> nextLayer = m_layers.get(layerNum + 1);

            for (int n = 0; n < hiddenLayer.size(); ++n) {
                hiddenLayer.get(n).calcHiddenGradients(nextLayer);
            }
        }

        // For all layers from outputs to first hidden layer,
        // update the connection weights
        for (int layerNum = m_layers.size() - 1; layerNum > 0; --layerNum) {
            Vector<Neuron> layer = m_layers.get(layerNum);
            Vector<Neuron> prevLayer = m_layers.get(layerNum - 1);

            for (int n = 0; n < layer.size() - 1; ++n) {
                layer.get(n).updateInputWeights(prevLayer);
            }
        }
    }

    @Override
    public Vector<Double> getResults(Vector<Double> resultVals) {
        resultVals.clear();

        for (int n = 0; n < m_layers.get(m_layers.size() - 1).size() - 1; ++n) {
            resultVals.add(m_layers.get(m_layers.size() - 1).get(n).getOutputVal());
        }

        return resultVals;
    }

    @Override
    public void getWeights(Vector<Double> weightVals) {
        weightVals.clear();

        // Iterate layers
        for (int l = 0; l < m_layers.size(); ++l) {
            // Iterate each neuron and get weights;
            for (int n = 0; n < m_layers.get(l).size(); ++n) {
                // TODO: Complete the code to get the connection weights
            }

        }
    }
}
