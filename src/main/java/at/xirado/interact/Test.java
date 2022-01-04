package at.xirado.interact;

public class Test
{
    public static void main(String[] args)
    {
        Interact interact = Interact.Builder.create("d12e491edc01539008f087fc366751bd8a00c9c05d7e450b6d0eac8bd51ad2e3")
                .setPort(8081)
                .setHost("127.0.0.1")
                .build();
    }
}
