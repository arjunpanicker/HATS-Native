#include <iostream>
#include <string>
#include <vector>
#include <utility>
#include <sstream>

#include "pre_utils.h"
#include "embedding.h"

namespace hats {
    void Embedding::train()
    {
        ftFasttext->train(ftArgs);
        ftFasttext->saveModel("model.h");
        ftFasttext->saveVectors("embeddings.vec");
    }

    fasttext::Vector Embedding::getWordEmbedding(std::string word)
    {
        fasttext::Vector vec(ftFasttext->getDimension());

        ftFasttext->getWordVector(vec, word);

        return vec;
    }

    fasttext::Vector Embedding::getSentenceEmbedding(std::string sentence)
    {
        // Cannot use getSentenceVector() of fasttext as it uses
        // input stream for getting the sentence
        // Used the same code from getSentenceVector() of fasttext
 
        fasttext::Vector resultVec(ftFasttext->getDimension());
        resultVec.zero();

        fasttext::Vector tempVec(ftArgs.dim);
        std::stringstream ss(sentence);
        std::string word;
        int32_t count = 0;
        while(ss >> word) {
            ftFasttext->getWordVector(tempVec, word);
            fasttext::real norm = tempVec.norm();
            if (norm > 0) {
                tempVec.mul(1.0 / norm);
                resultVec.addVector(tempVec);
                count++;
            }
            std::cout << word << std::endl;
            std::cout << resultVec << std::endl;
        }
        if (count > 0) {
            resultVec.mul(1.0 / count);
        }

        return resultVec;
    }
}