{
  description = "Flake to build and develop Spleef";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-24.05";
  };

  outputs = { self, nixpkgs, ... } @inputs:
    let
      forAllSystems = function:
        nixpkgs.lib.genAttrs [
          "x86_64-linux"
          "aarch64-linux"
          "x86_64-darwin"
          "aarch64-darwin"
        ]
          (system: function (import nixpkgs { inherit system; }));
    in
    {
      devShells = forAllSystems (pkgs: {
        default = pkgs.mkShell {
          packages = with pkgs; [
            maven
            jdk17_headless
          ];
        };
      });

      packages = forAllSystems (pkgs: rec {
        default = spleef;
        spleef = pkgs.callPackage
          ({ lib, maven, jdk17_headless }: maven.buildMavenPackage rec {
            pname = "Spleef";
            version = "1.5.2";

            src = ./.;

            mvnHash = "";

            mvnJdk = jdk17_headless;
            nativeBuildInputs = [
              jdk17_headless
            ];

            installPhase = ''
              mkdir -p $out/share/${pname}
              install -Dm644 target/${pname}-${version}.jar $out/share/${pname}
            '';

            meta = {
              description = "";
              homepage = "";
              license = lib.licenses.gpl3;
            };
          })
          { };
      });
    };
}
