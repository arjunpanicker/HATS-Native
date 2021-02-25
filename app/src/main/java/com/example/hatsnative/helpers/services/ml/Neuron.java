package com.example.hatsnative.helpers.services.ml;

import com.example.hatsnative.models.ENeuronType;
import com.example.hatsnative.models.INeuron;

import java.util.Vector;

public class Neuron implements INeuron {
    private double lr = 0.15;
    private double alpha = 0.5;
    private double m_outputVal;
    private ENeuronType neuronType;
    private double m_gradient;
    private int m_myIndex;
    private Vector<Connection> m_outputWeights;

    public Neuron(int numOutputs, int myIndex, ENeuronType neuronType) {
        for (int c = 0; c < numOutputs; ++c) {
            m_outputWeights.add(new Connection());
        }

        this.m_myIndex = myIndex;
        this.neuronType = neuronType;
    }

    @Override
    public double sumDOW(Vector<Neuron> nextLayer) {
        double sum = 0d;

        // Sum our contributions of the errors at the nodes we feed
        for (int n = 0; n < nextLayer.size() - 1; ++n) {
            sum += m_outputWeights.get(n).getWeight() * nextLayer.get(n).m_gradient;
        }

        return sum;
    }

    @Override
    public void feedForward(Vector<Neuron> prevLayer, boolean isOutputLayer) {
        double sum = 0d;

        // Sum the prev layer's outputs (which are our inputs)
        // Include the bias node from the previous layer
        for (int n = 0; n < prevLayer.size(); ++n) {
            sum += prevLayer.get(n).m_outputVal * prevLayer.get(n).m_outputWeights.get(m_myIndex).getWeight();

            m_outputVal = isOutputLayer ? sum : INeuron.transferFunction(sum);
        }
    }

    @Override
    public void calcOutputGradients(double targetVal) {
        double delta = targetVal - m_outputVal;
        m_gradient = delta * INeuron.transferFunctionDerivative(m_outputVal);
    }

    @Override
    public void calcHiddenGradients(Vector<Neuron> nextLayer) {
        double dow = sumDOW(nextLayer);
        m_gradient = dow * INeuron.transferFunctionDerivative(m_outputVal);
    }

    @Override
    public void updateInputWeights(Vector<Neuron> prevLayer) {
        for (int n = 0; n < prevLayer.size(); ++n) {
            Neuron neuron = prevLayer.get(n);
            double oldDeltaWeight = neuron.m_outputWeights.get(m_myIndex).getDeltaWeight();

            double newDeltaWeight =
                    // Individual input, magnified by the gradient and learning rate
                    lr
                    * neuron.getOutputVal()
                    * m_gradient
                    // Also add momentum = a fraction of the previous delta weight
                    + alpha
                    * oldDeltaWeight;

            neuron.m_outputWeights.get(m_myIndex).setDeltaWeight(newDeltaWeight);
            neuron.m_outputWeights.get(m_myIndex).setWeight(
                    neuron.m_outputWeights.get(m_myIndex).getWeight() + newDeltaWeight
            );
        }
    }

    public double getOutputVal() {
        return m_outputVal;
    }

    public void setOutputVal(double m_outputVal) {
        this.m_outputVal = m_outputVal;
    }

    public ENeuronType getNeuronType() {
        return neuronType;
    }

    public void setNeuronType(ENeuronType neuronType) {
        this.neuronType = neuronType;
    }
}
