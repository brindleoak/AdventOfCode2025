{
  description = "Beginner-friendly Java dev environment";

  inputs = { nixpkgs.url = "nixpkgs/nixos-25.05"; };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in {
      devShells.${system}.default = pkgs.mkShell {
        name = "java-dev";

        packages = with pkgs; [ openjdk21 gradle maven jdt-language-server ];

        env = { JAVA_HOME = pkgs.openjdk21.passthru.home; };

        shellHook = ''
          echo "🧩 Java dev shell active!"
          echo "JDK: $(java -version 2>&1 | head -n1)"
          echo "JAVA_HOME is set to: $JAVA_HOME"
        '';
      };
    };
}

