package com.demo;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class readersWritersSJN {
  private int readers = 0;
  private final Semaphore resourceAccess = new Semaphore(1);
  private final Semaphore readCountAccess = new Semaphore(1);
  private final PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<>();

  public static void main(String[] args) {
    readersWritersSJN manager = new readersWritersSJN();
    // Creando hilos para lectores y escritores
    new Thread(manager.new Reader(1, 3)).start();
    new Thread(manager.new Writer(2, 5)).start();
    new Thread(manager.new Reader(3, 2)).start();
    new Thread(manager.new Writer(4, 1)).start();
  }

  class Request implements Comparable<Request> {
    int id;
    int duration;

    public Request(int id, int duration) {
      this.id = id;
      this.duration = duration;
    }

    @Override
    public int compareTo(Request other) {
      return Integer.compare(this.duration, other.duration);
    }
  }

  class Reader implements Runnable {
    private final int id;
    private final int duration;

    public Reader(int id, int duration) {
      this.id = id;
      this.duration = duration;
    }

    @Override
    public void run() {
      try {
        queue.put(new Request(id, duration));
        while (true) {
          if (queue.peek().id == id) {
            readCountAccess.acquire();
            if (readers == 0) {
              resourceAccess.acquire();
            }
            readers++;
            readCountAccess.release();
            break;
          }
        }

        // Leyendo el recurso
        System.out.println("Reader " + id + " is reading for " + duration + " seconds.");
        Thread.sleep(duration * 1000);

        // Finalizando lectura
        readCountAccess.acquire();
        readers--;
        if (readers == 0) {
          resourceAccess.release();
        }
        readCountAccess.release();

        queue.take(); // Remover de la cola de prioridad
        System.out.println("Reader " + id + " finished reading.");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  class Writer implements Runnable {
    private final int id;
    private final int duration;

    public Writer(int id, int duration) {
      this.id = id;
      this.duration = duration;
    }

    @Override
    public void run() {
      try {
        queue.put(new Request(id, duration));
        while (true) {
          if (queue.peek().id == id) {
            resourceAccess.acquire();
            break;
          }
        }

        // Escribiendo en el recurso
        System.out.println("Writer " + id + " is writing for " + duration + " seconds.");
        Thread.sleep(duration * 1000);

        // Finalizando escritura
        resourceAccess.release();

        queue.take(); // Remover de la cola de prioridad
        System.out.println("Writer " + id + " finished writing.");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}