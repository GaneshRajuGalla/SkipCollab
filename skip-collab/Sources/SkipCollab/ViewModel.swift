//
//  ViewModel.swift
//
//
//  Created by Ganesh on 31/10/23.
//

import Foundation
import SwiftUI

class ViewModel: ObservableObject{
    
    @Published var repos:[Repo] = []
    var urlSting:String = "https://api.github.com/search/repositories?q=swift"
    
    
    //@MainActor
    func fetchRepose() async{
        do{
            let response = try await APIClient.apiRequest(urlString: urlSting)
            if let repos = response?.items{
                await MainActor.run {
                    self.repos = repos
                }
            }
        }catch{
            print(error)
        }
    }
    
    func refreshData(){
        Task{
            await fetchRepose()
        }
    }
}
