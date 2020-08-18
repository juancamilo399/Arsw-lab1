## Part I - Introduction to threads in JAVA

In agreement with the lectures, complete the classes CountThread, so that they define the life cycle of a thread that prints the numbers between A and B on the screen.
Complete the main method of the CountMainThreads class so that: 

1. Create 3 threads of type CountThread, assigning the first interval [0..99], the second [99..199], and the third [200..299]. 
   
   Metodo countThread
   
   ![](img/countThread.png)
   
   Metodo main
   
   ![](img/countThreadMain.png)

2. Start the three threads with start(). Run and check the output on the screen. 

   Usando el metodo start()
  
   ![](img/start.png)
   
   Usando el metodo run()
   
   ![](img/run.png)

3. Change the beginning with start() to run(). How does the output change? Why?\

   Usando start los hilos se ejecutan en un un orden diferente al especificado en el codigo, mientras que usando run los hilos se inician de acuerdo a la forma especificada en  el codigo. Esto sucede porque al usar el metodo start() se asigna un nuevo hilo y este llama al método run(), esto quiere decir que usando el metodo start() su método run se   ejecuta en hilos separados (simultaneamente), mientras que al llamar el método run() directamente no se crean multiples subprocesos por lo que la ejecución se refleja de forma
   simultanea.
   
# Part III - Discussion
The strategy of parallelism previously implemented is inefficient in certain cases, since the search is still carried out even when the N threads (as a whole) have already found the minimum number of occurrences required to report to the server as malicious. How could the implementation be modified to minimize the number of queries in these cases? What new element would this bring to the problem?.

La estrategia implementada anteriormente es ineficiente en ciertos casos porque pueden ocurrir busquedas cuando ya se han encontrado el número de ocurrencias necesarias para considerar un servidor malicioso, por lo tanto es necesario crear una variable compartida entre los distintos hilos cuya función sea llevar la cuenta de las distintas ocurrencias, pero esto trae un problema consigo ya que se crearia una condición de carrera, por lo que esta variable se volveria atómica, protegiendo de esta menera su integridad. 

# Part IV - Performance Evaluation 
From the above, implement the following sequence of experiments to perform the validation of dispersed IP addresses (for example 202.24.34.55), taking the execution times of them (be sure to do them on the same machine):
1. A single thread. 
2. As many threads as processing cores (have the program determine this using the Runtime API). 
3. As many threads as twice the number of processing cores. 
4. 50 threads 
5. 100 threads

When starting the program run the monitor jVisualVM, and as you run the tests, review and record the CPU and memory consumption in each case.

1. A single thread. 

![](img/thread1.png)
![](img/cpu1.PNG)

2. As many threads as processing cores. 8 Threads

El consumo de memoria es tan bajo que no es detectado

![](img/thread2.png)

3. As many threads as twice the number of processing cores. 16 Threads

El consumo de memoria es tan bajo que no es detectado

![](img/thread3.png)

4. 50 threads 

El consumo de memoria es tan bajo que no es detectado

![](img/thread4.png)

5. 100 threads

El consumo de memoria es tan bajo que no es detectado

![](img/thread5.png)

With the above, and with the given execution times, make a graph of solution time vs. Number of threads. Analyze and hypothesize with your partner for the following questions (you can take into account what was reported by jVisualVM):

![](img/grafica.PNG)

1. According to Amdahls law, where S(n) is the theoretical improvement of performance, P the parallel fraction of the algorithm, and n the number of threads, the greater n, the better this improvement should be. Why is the best performance not achieved with the 500 threads? How is this performance compared when using 200 ?.

Como sabemos, la ley de Amdahl define la ganancia del rendimiento que puede lograrse al introducir una mejora en nuestro procesador. Sin embargo, la segunda variante de esta ley nos menciona que el incremento del rendimiento será menor si se introduce una mejora sobre un sistema previamente mejorado.  En el caso de BlackListSearch esa mejora estará reflejada por el numero de Threads usados durante su ejecución.
Cuando se realizó la ejecución del programa con 1 hilo obtuvimos un rendimiento de 119899 milisegundos. Posteriormente , realizando la ejecución del programa con 8 Threads obtuvimos una mejora significativa al presentarse 1945 en el tiempo de ejecución. Es así , que a medida que aumentemos el numero de Threads , cada vez habrá un incremento del rendimiento menor en el programa. De esta manera , estaremos acercándonos hacia un tiempo de ejecución constante. Es así , que se espera un aumento en el rendimiento pasando de 200 a 500 Threads pero dicho aumento no será relevante a la hora de realizar la ejecución del programa.

2. How does the solution behave using as many processing threads as cores compared to the result of using twice as much?

Usando 8 Threads obtenemos un tiempo de 1945 milisegundos y usando 16 Threads obtenemos un tiempo de 1880 milisegundos. Estamos viendo en aplicación la segunda ley de Amdahl. Es decir  existe un punto en el que no importa el numero de Threads que aumentemos , no obtendremos un aumento significativo en el rendimiento. Así lo vemos reflejado en este caso en el que a pesar de usar el doble de threads de procesamiento no obtuvimos un aumento de mas de 100 milisegundos en el rendimiento.

3. According to the above, if for this problem instead of 100 threads in a single CPU could be used 1 thread in each of 100 hypothetical machines, Amdahls law would apply better ?. If x threads are used instead of 100/x distributed machines (where x is the number of cores of these machines), would it be improved? Explain your answer.

La ley de Amdahl define el aceleramiento del rendimiento que puede lograrse al introducir una mejora en una computadora. En la pregunta se plantea el uso de 100 computadores con cada una de ella a su mínimo rendimiento. En primer lugar, obtendríamos un aumento del rendimiento ya que no se está realizando ninguna mejora en ninguna de las computadoras. En segundo lugar, podríamos estar provocando una disminución en el rendimiento al tener que asegurar la coordinación entre todas las maquinas a la hora de acoplar resultados.

Como se menciono en el punto 1 de la parte 4, hay un punto en el que se alcanza un tiempo constante a pesar del aumento del numero de Threads durante la ejecución. En este caso, ese punto se puede ver reflejado durante la utilización de un numero de threads que es igual a cores del sistema. A medida que se aumente el numero de Threads desde este punto se obtendrá un aumento en el rendimiento en la ejecución del programa que no es significante para quien este utilizando la aplicación 
