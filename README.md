# ✨  Simple HTML Analyzer

I do not have time to test in all use cases.

I think it's a giant software for the time and would be better developed in group

> [!CAUTION]
>
> It's a Technical Challenge
>
> And it's not a project i'm willing to maintain

# ⚡️ Requirements

A machine running Linux, Windows or macOS.

If you're on a portable device, it may be possible to run with `termux`.
You can contribute for more details on it if desired.

If you're on `Ubuntu` Linux Distribution, you can use `apt install <package-name>`.

On NixOS, BSD or any other distro, you may `handle it yourself`.


# 📦 Dependencies or Installation

Please install `GNU Make` program.

Windows
```pwsh
$ scoop install make
$ scoop install openjdk17
```

Archlinux:
```sh
$ {paru,yay,[sudo pacman]} -S make jdk17-openjdk
```

MacOS
```sh
$ brew install make
$ brew install openjdk@17
```

Keep track of JAVA_HOME env variable, caution to not overwrite it.

# 🚀 Usage with GNU Make

Compiling:

```sh
$ make compile
```

Running Default Example:
```sh
$ make run
```

Running Batch Tests
```sh
$ make tests
```

# 🪓 Usage Without GNU Make

> [!WARNING]
> You'll need to run manually

Compiling
```sh
$ javac HTMLAnalyzer.java
```

Running default example:
```sh
$ java HTMLAnalyzer http://hiring.axreng.com/internship/example1.html
```

Running local examples:
```sh
$ java HTMLAnalyzer example.html

# optionally
$ java HTMLAnalyzer example[2,3,4,5,6,7].html
```

Keep track that `example1.html` is reserved for the default example.
In `Makefile` `download/page/html:` target, it fetches for it with `wget` just for printing the content in a `handy way`.

In the `run:` target, the program normally fetches for the web content and stores it as Java Objects in memory, following the `Challenge Specification`.

>[!NOTE]
> Fetch `docs/LOG.md` for last running output.
> Github repo:
> https://github.com/rafaeloledo/simple-html-analyzer
