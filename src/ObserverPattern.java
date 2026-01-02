//Design a Social Media Feed System where followers are notified when a user posts new content, with support for subscribe/unsubscribe at runtime.
//For each question:
//
//Identify the Subject -
//
//Identify the Observers
//
//Define the Observer interface
//
//Handle attach / detach
//
//Ensure loose coupling
//
//Decide between push vs pull model

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsible ONLY for creating posts
 * and generating unique post IDs.
 */
class PostService {

    // Thread-safe counter to auto-increment post IDs
    private final AtomicInteger counter = new AtomicInteger(0);

    // Factory method to create a new Post
    public Post createPost(String content) {
        int id = counter.incrementAndGet();
        return new Post(id, content);
    }
}

/**
 * Domain model representing a social media post.
 * Immutable by design.
 */
class Post {

    private final int id;
    private final String content;

    Post(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

/**
 * Observer interface (PUSH model).
 * Any class interested in receiving post updates
 * must implement this interface.
 */
interface ObserverUsers {

    // Called by Subject when a new post is published
    void update(Post post);
}

/**
 * Base User class acting as an Observer.
 * Maintains a personal feed of received posts.
 */
class User implements ObserverUsers {

    // Feed that stores all received posts
    private List<Post> feed;

    public User() {
        feed = new ArrayList<>();
    }

    // Subscribe this user to a Subject (FeedPublisher)
    public void subscribeToSubject(SubjectUsers subject) {
        subject.subscribe(this);
    }

    // Unsubscribe this user from a Subject
    public void unsubscribeFromSubject(SubjectUsers subject) {
        subject.unsubscribe(this);
    }

    // Called automatically when Subject publishes a post
    @Override
    public void update(Post post) {
        feed.add(post);
    }

    // Returns the user's feed
    public List<Post> getFeed() {
        return feed;
    }
}

/**
 * Concrete user types.
 * Currently they share behavior, but can be extended later.
 */
class LakshmanUser extends User {}
class JeyaUser extends User {}
class ThirumurthiUser extends User {}

/**
 * Subject interface.
 * Defines subscription management operations.
 */
interface SubjectUsers {

    // Register an observer
    void subscribe(ObserverUsers observer);

    // Remove an observer
    void unsubscribe(ObserverUsers observer);
}

/**
 * Concrete Subject implementation.
 * Publishes posts and notifies all subscribed observers.
 */
class FeedPublisher implements SubjectUsers {

    // List of all subscribed observers
    private final List<ObserverUsers> observers = new ArrayList<>();

    // Service used to create posts
    private final PostService postService;

    public FeedPublisher(PostService postService) {
        this.postService = postService;
    }

    // Add observer to subscriber list
    @Override
    public void subscribe(ObserverUsers observer) {
        observers.add(observer);
    }

    // Remove observer from subscriber list
    @Override
    public void unsubscribe(ObserverUsers observer) {
        observers.remove(observer);
    }

    // Creates a post and notifies all observers
    public void publish(String content) {
        Post post = postService.createPost(content);
        notifyObservers(post);
    }

    // Pushes the post to all observers
    private void notifyObservers(Post post) {
        for (ObserverUsers observer : observers) {
            observer.update(post);
        }
    }
}


public class ObserverPattern
{
    private static void printUserFeed(String userName,  User user) {
        System.out.println("Feed of " + userName + ":");

        if (user.getFeed().isEmpty()) {
            System.out.println("  (No posts)");
        } else {
            for (Post post : user.getFeed()) {
                System.out.println("  Post ID: " + post.getId());
                System.out.println("  Content: " + post.getContent());
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println("=== Social Media Feed Simulation ===\n");

        // Create publisher
        FeedPublisher feedPublisher = new FeedPublisher(new PostService());

        // Create users
        LakshmanUser lakshman = new LakshmanUser();
        JeyaUser jeya = new JeyaUser();
        ThirumurthiUser thiru = new ThirumurthiUser();

        // Users subscribe
        System.out.println("Users subscribing to feed...\n");
        lakshman.subscribeToSubject(feedPublisher);
        jeya.subscribeToSubject(feedPublisher);
        thiru.subscribeToSubject(feedPublisher);

        // First post
        System.out.println("Jogesh posts first content...\n");
        feedPublisher.publish("Hi, How are you!! Brownie was tasty.");

        printUserFeed("Lakshman", lakshman);
        printUserFeed("Jeya", jeya);
        printUserFeed("Thirumurthi", thiru);

        // Unsubscribe one user
        System.out.println("Thirumurthi unsubscribes from feed...\n");
        thiru.unsubscribeFromSubject(feedPublisher);

        // Second post
        System.out.println("Jogesh posts second content...\n");
        feedPublisher.publish("Cricket is interesting");

        printUserFeed("Lakshman", lakshman);
        printUserFeed("Jeya", jeya);
        printUserFeed("Thirumurthi (should not receive new post)", thiru);

        System.out.println("\n=== Simulation Complete ===");
    }
}
