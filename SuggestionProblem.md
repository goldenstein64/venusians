# The Suggestion Problem

Say we have two sets of objects. One is the approved set, and the other is the suggested set.

The suggested set can be approved or rejected. Accepting a suggestion is the same as merging the suggested set into the approved set, and rejecting a suggestion is the same as discarding the suggested set.

The suggested set can contain both new objects and objects deleted in the approved set. In other words, the suggested set is a delta of the approved set such that accepting a suggestion would be equivalent to applying all the changes in the delta.

What ways are there to represent these two sets?

- Two sets, one containing only real objects, and other containing new objects and actions done to real objects
- One set where some of its elements have a `suggested` or `deleted` flag. Applying changes to the set would be the same as neutralizing their flags.
- Two sets, one containing the "level" of objects, and the other containing deltas to those levels.