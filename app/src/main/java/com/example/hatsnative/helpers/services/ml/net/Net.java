package com.example.hatsnative.helpers.services.ml.net;

import android.util.Log;

import com.example.hatsnative.models.ml.ENeuronType;
import com.example.hatsnative.models.ml.INet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Net implements INet {
    Vector<Vector<Neuron>> m_layers = new Vector<>();  // m_layers[layerNum][neuronNum]
    double m_error;
    double m_recentAverageError;
    double m_recentAverageSmoothingFactor = 100.0;

    public static Vector<Integer> topology;

    private static Net instance = null;

    public static Net getInstance(Vector<Integer> topology, JSONObject weights) {
        if (instance == null) {
            instance = new Net(topology, weights);
        }

        return instance;
    }

    private Net(Vector<Integer> mytopology, JSONObject weights) {
        topology = mytopology;
        int numLayers = topology.size();

        try {
            for(int layerNum = 0; layerNum < numLayers; ++layerNum) {
                m_layers.add(new Vector<>());
                int numOutputs = layerNum == numLayers - 1 ? 0 : topology.get(layerNum + 1);
                JSONObject layerWeights = weights != null && layerNum != numLayers - 1 ?
                        (JSONObject) weights.get(String.valueOf(layerNum)) : null;

                ENeuronType nType;
                if (layerNum == 0) {
                    nType = ENeuronType.INPUT;
                } else if (layerNum == numLayers - 1) {
                    nType = ENeuronType.OUTPUT;
                } else {
                    nType = ENeuronType.HIDDEN;
                }

                // We have made a new Layer. Now, we need to add neurons to the layer.
                // We also need to add the bias neuron to each layer
                for (int neuronNum = 0; neuronNum <= topology.get(layerNum); ++neuronNum) {
                    m_layers.lastElement().add(new Neuron(numOutputs, neuronNum, nType,
                            layerWeights != null ?
                                    (JSONArray) layerWeights.get(String.valueOf(neuronNum)) : null));
                }
                Log.d("NN", "Layer " + (layerNum + 1) + ": " + (topology.get(layerNum)));

                // Force the bias node's output value to 1.0. It's the last neuron created above
                m_layers.lastElement().lastElement().setOutputVal(1.0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
        if (targetVals.size() != m_layers.lastElement().size() - 1) return;

        // Calculate overall net error (RMS of output errors)
        Vector<Neuron> outputLayer = m_layers.lastElement();
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
    public Vector<Double> getResults() {
        Vector<Double> resultVals = new Vector<>();

        for (int n = 0; n < m_layers.lastElement().size() - 1; ++n) {
            resultVals.add(m_layers.lastElement().get(n).getOutputVal());
        }

        return resultVals;
    }

    @Override
    public LinkedHashMap<Integer, List<List<Double>>> getWeights() {
        LinkedHashMap<Integer, List<List<Double>>> weightVals = new LinkedHashMap<>();

        // Iterate layers
        for (int l = 0; l < m_layers.size(); ++l) {
            // Iterate each neuron in layer l and get weights for each connection
            List<List<Double>> layerWeights = new ArrayList<>();
            for (int n = 0; n < m_layers.get(l).size(); ++n) {
                Vector<Connection> connections = m_layers.get(l).get(n).getConnections();
                List<Double> weights = connections.stream().map(connection -> connection.getWeight())
                        .collect(Collectors.toList());
                layerWeights.add(weights);
            }

            weightVals.put(l, layerWeights);
        }

        return weightVals;
    }
}
