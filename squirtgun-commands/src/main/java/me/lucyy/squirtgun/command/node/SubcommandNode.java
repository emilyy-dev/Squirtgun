package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.lucyy.squirtgun.command.argument.CommandArgument;
import me.lucyy.squirtgun.command.argument.ListArgument;
import me.lucyy.squirtgun.command.context.CommandContext;
import me.lucyy.squirtgun.platform.PermissionHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A node that tab completes and acts as a "splitter" for other nodes.
 * As per the name, it can be used to create subcommands.
 *
 * @since 2.0.0
 */
public class SubcommandNode<T extends PermissionHolder> implements CommandNode<T> {

    private final Set<? extends CommandNode<T>> childNodes;
    private final String name;
    private final @Nullable String permission;
    private final CommandArgument<String> argument;
    private final CommandNode<T> helpNode;

    /**
     * @param name        this node's name.
     * @param permission  the permission needed to execute this node. May be null if none is required.
     * @param addHelpNode whether or not to add a {@link SubcommandHelpNode} with the name "help" to the list
     * @param childNodes  the child nodes
     */
    @SafeVarargs
    public SubcommandNode(@NotNull String name, @Nullable String permission,
                          boolean addHelpNode, @NotNull CommandNode<T>... childNodes) {
        Preconditions.checkNotNull(childNodes, "Child nodes must not be null");
        Preconditions.checkNotNull(name, "Name must not be null");

        Set<CommandNode<T>> nodes = new HashSet<>(Arrays.asList(childNodes));

        this.childNodes = nodes;
        this.name = name;
        this.permission = permission;

        if (addHelpNode) {
            helpNode = new SubcommandHelpNode<>(this);
            nodes.add(helpNode);
        } else {
            helpNode = null;
        }

        argument = new ListArgument("subcommand", "The subcommand to execute",
                addHelpNode,
                this.childNodes.stream()
                        .map(CommandNode::getName)
                        .collect(Collectors.toList()));
    }

    private @Nullable CommandNode<T> getNode(String name) {
        return childNodes.stream().filter(x -> x.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return ImmutableList.of(argument);
    }

    /**
     * Gets the child nodes that this node holds.
     */
    public Set<? extends CommandNode<T>> getNodes() {
        return childNodes;
    }

    @Override
    public @Nullable CommandNode<T> next(CommandContext<T> context) {
        String name = context.getArgumentValue(argument);
        return name == null ? helpNode : getNode(name);
    }

    @Override
    public @Nullable Component execute(CommandContext<T> context) {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    } // TODO

    @Override
    public @Nullable String getPermission() {
        return permission;
    }
}
