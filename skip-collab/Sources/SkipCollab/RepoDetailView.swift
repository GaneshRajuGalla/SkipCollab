//
//  File.swift
//  
//
//  Created by Ganesh on 31/10/23.
//

import Foundation
import SwiftUI

struct RepoDetailView: View{
    let owner:Owner?
    
    var body: some View{
        VStack(spacing:20.0) {
            
            AsyncImage(url: URL(string: owner?.avatarURL ?? "")) { image in
                image
                    .resizable()
                    .frame(width:200.0, height: 200.0)
            } placeholder: {}
            
            Text(owner?.login ?? "")
                .font(.title)
                .fontWeight(.bold)
        }
    }
}
