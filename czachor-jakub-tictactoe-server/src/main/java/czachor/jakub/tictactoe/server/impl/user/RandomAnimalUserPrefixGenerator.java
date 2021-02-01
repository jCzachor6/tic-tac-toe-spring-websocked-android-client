package czachor.jakub.tictactoe.server.impl.user;

import generic.online.game.server.gogs.utils.AnonymousUsernameGenerator;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomAnimalUserPrefixGenerator implements AnonymousUsernameGenerator {
    private int id;
    private final Random random = new Random(new Date().getTime());
    private final List<String> animals = Arrays.asList(
            "Cow", "Rabbit", "Duck", "Shrimp", "Pig",
            "Goat", "Crab", "Deer", "Bee", "Sheep",
            "Fish", "Turkey", "Dove", "Chicken", "Horse",
            "Crow", "Peacock", "Dove", "Sparrow", "Goose",
            "Mouse", "Kangaroo", "Goat", "Horse", "Monkey",
            "Cow", "Koala", "Mole", "Elephant", "Leopard",
            "Hippopotamus", "Giraffe", "Fox",
            "Coyote", "Hedgehog", "Sheep", "Deer");

    @Override
    public String generate() {
        return animals.get(random.nextInt(animals.size())) + "#" + ++id;
    }
}
