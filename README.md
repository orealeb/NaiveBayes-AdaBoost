NaiveBayes-AdaBoost
===================

NaiveBayes and AdaBoost Implementation


I implemented two algorithms in my classification framework. NaiveBayes and Adaboost (as an extension of NaiveBayes).


Naive Bayes methods are a set of supervised learning algorithms based on applying Bayes’ theorem with the “naive” assumption of independence between every pair of features. I implemented a binary or binomial classification, which classifies the elements of a given set into two groups on the basis of a classification rule. In addition, all attributes in the expected datasets are treated as categorical attributes by my algorithm. You can take a look at wikipedia for more details about the algorithm.

AdaBoost, (Adaptive Boosting) is a boosting approach in machine learning based on the idea of creating a highly accurate prediction rule by combining many relatively weak and inaccurate rules. I implemented AdaBoost and NaiveBayes as a classifier interface in my framework, however, AdaBoost doesn’t have its own classification algorithm, rather it runs the NaiveBayes algorithm several times to create multiple weak classifiers which are then combined based on their error rates in order to improve the overall performance of the NaiveBayes classifier.
