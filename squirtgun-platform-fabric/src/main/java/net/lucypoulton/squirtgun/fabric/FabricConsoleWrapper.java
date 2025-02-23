/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.lucypoulton.squirtgun.fabric;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.lucypoulton.squirtgun.platform.audience.SquirtgunUser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

/**
 * Simple Squirtgun wrapper for the Fabric console "user".
 */
public final class FabricConsoleWrapper implements SquirtgunUser, ForwardingAudience.Single {

    private final FabricPlatform platform;

    FabricConsoleWrapper(final FabricPlatform platform) {
        this.platform = platform;
    }

    @Override
    public boolean hasPermission(final String permission) {
        // console being console, if the permission is undefined, it should always fallback to true
        return Permissions.check(this.platform.getServer().getCommandSource(), permission, true);
    }

    @Override
    public @NotNull Audience audience() {
        return this.platform.getAudienceProvider().console();
    }
}
