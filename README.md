jacce
=====

Jacce is a testbed for various caching algorithms.
It's structured in such way as to allow easy creation, testing, and bench-marking of new cache implementations.

Implemented algorithms:

* CAR - CLOCK with Adaptive Replacement
* LRU
* FIFO

All implementations have a thread-safe version and support timed eviction policy.

### References

* Sorav Bansal, Dharmendra S. Modha. CAR: Clock with Adaptive Replacement. Proceedings of the 3rd USENIX Conference on File and Storage Technologies, Pages 187 - 200, 2004. [link](https://www.usenix.org/legacy/publications/library/proceedings/fast04/tech/full_papers/bansal/bansal.pdf)

### Author

Written by Roman Ovseitsev <romovs@gmail.com>

### License

Released under GPLv2. See LICENSE file.
