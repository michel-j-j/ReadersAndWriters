# Readers-Writers Problem with Shortest Job Next (SJN) Strategy

## 📋 Problem Statement

The **Readers-Writers Problem** is a classic synchronization problem in concurrent programming that deals with managing access to a shared resource (like a database) by multiple readers and writers:

- **Readers** can access the resource simultaneously without problems, as they do not modify the data.
- **Writers**, however, need exclusive access to the resource to avoid conflicts and ensure data consistency.

### ⚠️ The Challenge
- **Prioritization**: Writers need exclusive access, which can lead to **starvation** for either readers or writers if one group is always prioritized.
- **Concurrency**: Allowing multiple readers while preventing simultaneous writers requires careful synchronization.

In this project, we take it a step further by implementing a **Shortest Job Next (SJN)**-like strategy to prioritize the shortest requests (whether reading or writing) first. This helps minimize the average waiting time for access to the shared resource.

## 🚀 Implementation Overview

This Java implementation uses **semaphores** and **priority queues** to address the Readers-Writers problem, aiming to prioritize requests based on their duration — inspired by the **Shortest Job Next** (SJN) scheduling algorithm.

### ⚙️ How It Works

- **Readers and Writers**:
  - **Readers** can access the resource concurrently if no writers are active.
  - **Writers** require exclusive access to the resource, meaning no other readers or writers can access it at the same time.

- **Prioritization**:
  - Each reader or writer has a **request** with a specific duration.
  - Requests are placed in a **priority queue** that ensures the shortest duration requests are given priority over longer ones.
  
### 🔧 Tools and Technologies

- **Java Concurrency**: Uses `PriorityBlockingQueue` for request management and `Semaphore` for synchronization.
- **Threading**: Both readers and writers are implemented as Java `Runnable` classes and executed as separate threads.

## 🗂️ Code Structure

### 1. Class `ReadersWritersSJN`
- **`PriorityBlockingQueue<Request>`**: Stores requests from readers and writers, ordered by duration to ensure shorter tasks are prioritized.
- **`Semaphore resourceAccess`**: Controls exclusive access to the shared resource, ensuring that only one writer or multiple readers can access it at any time.
- **`Semaphore readCountAccess`**: Manages access to the count of readers (`readers`) to ensure safe concurrent updates.

### 2. Class `Request`
- Represents a request to access the shared resource.
- Implements `Comparable` to define ordering based on **duration**, allowing the priority queue to sort them.

### 3. Classes `Reader` and `Writer`
- Both extend `Runnable` and are executed in separate threads.
- **`Reader`**: Reads the resource and can do so concurrently with other readers if no writers are active.
- **`Writer`**: Requires exclusive access to the resource, ensuring data consistency.
- **Access Control Flow**:
  - Each reader and writer adds their request to the **priority queue**.
  - Each thread waits until its request is at the **front** of the queue, ensuring that shorter tasks are prioritized.

## 💡 Key Features

1. **Shortest Job Next (SJN) Prioritization**: 
   - Requests are prioritized based on their estimated **duration**. This helps minimize the **average wait time**, similar to how SJN works in CPU scheduling.

2. **Safe Concurrent Access**:
   - **Readers** can access the resource simultaneously.
   - **Writers** gain **exclusive access**, ensuring no readers are present.

3. **Java Semaphores and Priority Queue**:
   - The use of **semaphores** (`resourceAccess`, `readCountAccess`) ensures proper synchronization between readers and writers.
   - The **priority queue** (`PriorityBlockingQueue<Request>`) manages access prioritization.

## 🔨 Getting Started

### Prerequisites
- **Java 11 or later**: The code is written in Java, so you'll need to install the JDK.
- **IDE (Optional)**: An IDE like IntelliJ IDEA, Eclipse, or Visual Studio Code is recommended for easier code management.

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/readers-writers-sjn.git
    cd readers-writers-sjn
    ```
2. Compile the Java files:
    ```bash
    javac ReadersWritersSJN.java
    ```
3. Run the program:
    ```bash
    java ReadersWritersSJN
    ```

## 📚 Usage Example

```java
public static void main(String[] args) {
    ReadersWritersSJN manager = new ReadersWritersSJN();
    // Creating threads for readers and writers
    new Thread(manager.new Reader(1, 3)).start(); // Reader 1, reading for 3 seconds
    new Thread(manager.new Writer(2, 5)).start(); // Writer 2, writing for 5 seconds
    new Thread(manager.new Reader(3, 2)).start(); // Reader 3, reading for 2 seconds
    new Thread(manager.new Writer(4, 1)).start(); // Writer 4, writing for 1 second
}
```

- In this example:
  - **Writer 4** (duration 1s) will be given priority over the rest.
  - **Reader 3** (duration 2s) will be prioritized over **Reader 1** and **Writer 2**.

## 🌟 Why This Approach?

Using an SJN-like strategy to handle the **Readers-Writers Problem** offers a balanced approach to minimizing the waiting time for requests while ensuring **fair and synchronized access** to the shared resource.

- **Reduced Waiting Time**: Shorter tasks are prioritized, which can help in reducing the **overall waiting time**.
- **Starvation Prevention**: Although SJN can sometimes lead to **starvation** for long requests, the use of a **queue** in our approach helps mitigate this by ensuring fairness.

## 🤝 Contributing

Contributions are welcome! Feel free to fork this repository, submit issues, or open a pull request.

### To Contribute:
1. **Fork the repository**.
2. **Create a branch** for your feature (`git checkout -b feature/YourFeature`).
3. **Commit your changes** (`git commit -m 'Add some feature'`).
4. **Push to the branch** (`git push origin feature/YourFeature`).
5. **Open a pull request**.

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
