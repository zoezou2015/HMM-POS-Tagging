# POS_HMM

Baseline

For each word, if the word occurred in training corpus, then find the most frequent tag for it. Otherwise, get the most frequent tag 
which occurs most times in global training corpus.

HMM Parser

Using count collecting from training corpus to calculate transition and emission probability, then call viterbi to traceback
