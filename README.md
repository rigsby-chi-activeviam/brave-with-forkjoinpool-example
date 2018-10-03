## Brave with ForkJoinPool Example

The example demonstrates a way to use Brave with `ForkJoinPool`. This example is created on top of the [brave-webmvc-example](https://github.com/openzipkin/brave-webmvc-example)

### Implementation
The example demonstrates a way to use Brave with `ForkJoinPool` by extending the `CurrentTraceContext`. This project mainly introduce the following classes:  

`ATraceableForkJoinTask`: Wrapper of `ForkJoinTask`  
  
`ATraceableForkJoinPool`: Extending `ForkJoinPool` with task wrapping inside methods like `sumbit` and `invoke`  
  
`CustomizedThreadLocalCurrentTraceContext`: Extending `CurrentTraceContext` and provides a `getTraceableForkJoinPool` method, which returns a new traceable `ForkJoinPool` instance.  
  

### Starting the Application

Execute below, then access http://localhost:8080/. This will call the service and produce traced logs.
```
mvn jetty:run
```

### Limitation

Trace context can be propagated only if a task is submitted to the pool (e.g. `pool.submit(task)`). If a task forks, the trace will be propagated (e.g. `task.fork()`).
  
