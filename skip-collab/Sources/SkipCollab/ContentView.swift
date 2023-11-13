import SwiftUI

struct ContentView: View {
    
    @StateObject var viewModel = ViewModel()

    var body: some View {
        NavigationStack {
            List(viewModel.repos, id: \.id) { repo in
                NavigationLink(value: repo) {
                    listRow(repo: repo)
                }
            }
            .navigationDestination(for: Repo.self, destination: { repo in
                RepoDetailView(owner: repo.owner)
            })
            .navigationTitle("Repos")
            .task {
                await viewModel.fetchRepose()
            }
        }
    }
}


extension ContentView {
    
    private func listRow(repo:Repo) -> some View{
        Label {
            Text(repo.repoName ?? "")
        } icon: {
            AsyncImage(url: URL(string: repo.owner?.avatarURL ?? "")) { image in
                image
                    .resizable()
                    .frame(width:32.0, height: 32.0)
            } placeholder: {
                // ProgressView()
            }
        }
    }
}
