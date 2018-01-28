# TankWar

This is the continuation of  [TankWar][tankwar].

[tankwar]: https://github.com/ColOfAbRiX/tankwar

## A study on Neuroevolution

> "Neuroevolution, or neuro-evolution, is a form of machine learning that uses evolutionary algorithms to train artificial neural networks. It is most commonly applied in artificial life, computer games, and evolutionary robotics." - Wikipedia

## The TankWar Project

### A brief description

The project is a simulation of a battlefield where small _"alive"_ beings (to be intended in [this broad sense][turing-test]), named Tanks, fight between each others for their survival and to transmit their genes to the next generation.

Their movements and their behaviours are not hard coded but determined using a neural network, evolution and rules.

At the beginning of the simulation every tank is "stupid" and does more or less nothing interesting. Generation after
generation the Tanks' network mutates creating new behaviours and new interactions with the world. Usually these changes are useless for the final score -fitness- of the Tank but sometimes, here and there, a behaviour appears that proves to be useful for the Tank, increasing its score. Useful behaviours are spread across generations and accumulated on individuals so that recognizable behaviours can be seen, like Tanks dodging bullets, following and targeting other Tanks and even close-combat scenes!

This is an optimization process aimed at maximising a "life function".

[turing-test]: https://en.wikipedia.org/wiki/Turing_test

### History

The project was first inspired reading, years ago, the article [Evolution of Adaptive Behaviour in Robots by Means of
Darwinian Selection][PLOSS-1]. In the article some algorithms were first trained on a computer, using a combination of
neural networks to control their behaviour, and evolutionary algorithm to train the network themselves. The resulting
data was then implemented into real robot that started to fight between each others exposing recognizable behaviours and patterns.

After few years of relative laziness, I took the opportunity with one of my students to develop this idea into a teaching
project. With the oncoming of my interest for the Scala Programming Language I decided to start myself a side project to
that first one and properly implement my own ideas on the subject and learn the language itself.

The first implementation of the project has been a success, I could see different behaviours evolving after only few
tens of generations but at the same time all my inexperience on the domain and on the programming style made the code
fairly complex and messy, also because I decided to implement by myself the physics and nerual network algorithms.
For these reasons and for the fact that I wanted more generalization and to be able to play around in different
situations, I decided to fully rewrite the projct using more funcional programming and libraries, except for the
graphics that I think it's small enough that it's worth the effor of building it.

[PLOSS-1]: http://journals.plos.org/plosbiology/article?id=10.1371/journal.pbio.1000292

### Goals

Tankwar is mainly an educational project for fun.

TankWar 2 is a different project than it's parent [TankWar][tankwar].

The main goal is to learn the basics of Artificial Intelligence and related topic, like [Artificial Neural Networks][WIKI-1] and [Genetic Algorithms][WIKI-2]. And to be honest, to see my little tanks evolve and how they live their artificial
lives in the arena.

##Quick Start

For quick start read the [Getting Started](CONTRIBUTING.md) section in the **CONTRIBUTING.md** file

## Topics touched by the project

### Neural Networks

### Genetic Algorithms

### Programming

* Scala programming language
  * Design patterns in scala
  * SBT
* Functional programming
  * Immutability
  * Pure code
  * Scalaz library
  * Monads and Monads Transformers

### Topics of Computer Graphics
