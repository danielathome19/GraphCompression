# About
This respository contains a new set of lossy and lossless graph compression techniques for external memory representation. To find out more, check out the provided research papers:
  * "Graph Compression - Toward a Generalized Algorithm.pdf" (DOI: 10.5281/zenodo.6600816)
 
# Usage
For data used in my experiments:
  * All datasets can be found on Kaggle (see provided research paper)
  * See **Program/Data** folder

**NOTE:** these folders should be placed in the **same** folder as "Main.java". For folder existing conflicts, simply merge the directories.


In Main.java, the "main" function acts as the controller for the system to run evaluations, and each component is separated by its own respective class file which may be called individually as well.

To choose an operation or series of operations for the model to perform, simply edit the fpmain function before running. Examples of all function calls can be seen commented out within fpmain.

# Bugs/Features
Bugs are tracked using the GitHub Issue Tracker.

Please use the issue tracker for the following purpose:
  * To raise a bug request; do include specific details and label it appropriately.
  * To suggest any improvements in existing features.
  * To suggest new features or structures or applications.

# Citation
If you use this code for your research, please cite this project:
```
@software{Szelogowski_GraphCompression_2022,
 author = {Szelogowski, Daniel},
 doi = {10.5281/zenodo.6600816},
 month = {5},
 title = {{GraphCompression}},
 license = {CC0-1.0},
 url = {https://github.com/danielathome19/GraphCompression},
 version = {1.0.0},
 year = {2022}
}
```
