#pragma once
#ifndef EMBEDDING_H
#define EMBEDDING_H

#include "Fasttext.h"

namespace hats {

    class Embedding {
        public:
            std::shared_ptr<fasttext::FastText> ftFasttext;
            fasttext::Args ftArgs;

            Embedding(std::string filename) {
                ftArgs = fasttext::Args();
                ftFasttext = std::make_shared<fasttext::FastText>();
                ftArgs.input = filename;
            }
            
            ~Embedding() { }

            // Train the fasttext model
            void train();
    
            // Get word embedding
            fasttext::Vector getWordEmbedding(std::string word);

            fasttext::Vector getSentenceEmbedding(std::string sentence);
    };
}

#endif // !EMBEDDING_H