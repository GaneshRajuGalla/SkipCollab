//
//  File.swift
//  
//
//  Created by Ganesh on 31/10/23.
//

import Foundation
import SwiftUI

import Foundation

// MARK: - ReposModel
struct ReposModel: Codable {
    var items: [Repo]?

    enum CodingKeys: String, CodingKey {
        case items
    }
}

// MARK: - Item
struct Repo: Codable,Hashable,Identifiable {
    let id: Int
    let repoName: String?
    let owner: Owner?
    let htmlURL: String?
    let itemDescription: String?
    let languagesURL: String?
    let stargazersCount: Int?
    let language: String?
    let forksCount: Int?
    let openIssuesCount: Int?
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    static func == (lhs: Repo, rhs: Repo) -> Bool {
        lhs.id == rhs.id
    }

    enum CodingKeys: String, CodingKey {
        case id = "id"
        case repoName = "name"
        case owner
        case htmlURL = "html_url"
        case itemDescription = "description"
        case languagesURL = "languages_url"
        case stargazersCount = "stargazers_count"
        case language
        case forksCount = "forks_count"
        case openIssuesCount = "open_issues_count"
    }

}


// MARK: - Owner
struct Owner: Codable, Hashable {
    let login: String?
    let avatarURL: String?
    let url: String?
    let htmlURL: String?
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(htmlURL)
    }
    
    static func == (lhs: Owner, rhs: Owner) -> Bool {
        lhs.htmlURL == rhs.htmlURL
    }


    enum CodingKeys: String, CodingKey {
        case login
        case avatarURL = "avatar_url"
        case url
        case htmlURL = "html_url"
    }
}
