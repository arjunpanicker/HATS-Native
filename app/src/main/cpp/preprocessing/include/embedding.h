#pragma once
#ifndef EMBEDDING_H
#define EMBEDDING_H

#include "Fasttext.h"

namespace hats {

    class Embedding {
        public:
            std::shared_ptr<fasttext::FastText> ftFasttext;
            fasttext::Args ftArgs;

            Embedding(const std::string &filename, const int &dims) {
                ftArgs = fasttext::Args();
                ftFasttext = std::make_shared<fasttext::FastText>();
                ftArgs.input = filename;
                ftArgs.dim = dims;
            }

            // Load the model from a saved file
            Embedding(const std::string &filename) {
                ftArgs = fasttext::Args();
                ftFasttext = std::make_shared<fasttext::FastText>();

                ftFasttext->loadModel(filename);
                ftArgs.dim = 50;
            }
            
            ~Embedding() { }

            // Train the fasttext model
            void train();
    
            // Get word embedding
            fasttext::Vector getWordEmbedding(const std::string &word);

            fasttext::Vector getSentenceEmbedding(const std::string &sentence);
    };
}

#endif // !EMBEDDING_H