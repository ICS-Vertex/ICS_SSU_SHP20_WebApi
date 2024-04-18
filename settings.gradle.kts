rootProject.name = "shp.ssu.icsvertex.nl.ics_ssu_shp20_webapi"
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    repositories {
        mavenCentral()

        maven("https://maven.pkg.github.com/ICS-Vertex/ICS_Kotlin_Modules") {
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_KEY")
            }
        }
        maven("https://maven.pkg.github.com/ICS-Vertex/ICS_Ktor_Modules") {
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_KEY")
            }
        }
        maven("https://maven.pkg.github.com/ICS-Vertex/ICS_SSU_SHP20_Core") {
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_KEY")
            }
        }
    }
}