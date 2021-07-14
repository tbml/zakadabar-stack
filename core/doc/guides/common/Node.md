# Node

A node is an actual, running application instance. It covers both frontend and
backend. Frontend running in a browser, backend serving that browser, another 
backend that generates reports: are all nodes.

Zakadabar helps you build a distributed application. It may have many active
nodes at the same time.

On the other side, one virtual machine - one node. The virtual machine here
means a JVM, a browser tab, an Android activity, an operating system process
for Native. In other words: nodes do not share memory. They communicate through
APIs, using the communication adapters the stack provides.

Nodes may present a user interface: a browser tab, a mobile client, a desktop 
application. However, this entirely depends on the node itself. Technically
you can run a node in browser worker.

