{
  description = "Beginner-friendly Java dev environment";

  inputs = {
    nixpkgs.url =
      "nixpkgs/nixos-25.05"; # or "nixos-unstable" if you prefer bleeding-edge
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in {
      devShells.${system}.default = pkgs.mkShell {
        name = "java-dev";

        packages = with pkgs; [
          # Java runtime & compiler
          openjdk21 # current long-term-support JDK

          # Build tools
          gradle
          maven

          # Optional helpers
          jdt-language-server # backend for Java completion/analysis
        ];

        # Nice-to-have shell settings
        shellHook = ''
          echo "🧩 Java dev shell active!"
          echo "JDK: $(java -version 2>&1 | head -n1)"
          echo "Use 'gradle init' or 'mvn archetype:generate' to start a new project."
        '';
      };
    };
}

