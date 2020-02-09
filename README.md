# DistrFileSys
## Notes from First Meeting
List of Commands:

cd - functions same as unix cd

ls - functions same as unix ls

cat - functions same as unix cat

rm - functions same as unix rm

mv - functions same as unix mv

cp - functions same as unix cp

push - push file to dfs from local machine

pull - pull file from dfs to local machine

Replication of *external* file system on all nodes

Indicator on file systems within each node indicating files/folders that are used in the dfs (ex: dfs-)

Tree structure to represent file system. Each node has a 2-tuple of the name of folder/file and an array of nodes that have that folder/file

Master node will have a lookup table consistening of a key that is the node identifier and a value that is a tree similar to the file system representation

We will be using Java

Use a conig file that is not pushed to the repository ever to store IP/port combination for access to nodes

heartbeat for node health detection

Start with full replication, then move to n-factor replication
