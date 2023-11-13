//
//  File.swift
//  
//
//  Created by Ganesh on 31/10/23.
//

import Foundation

class APIClient{
    
    static func apiRequest(urlString:String) async throws -> ReposModel?{
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidUrl
        }
        
        let (data,response) = try await URLSession.shared.data(from: url)
        
        guard let response = response as? HTTPURLResponse , (200..<300).contains(response.statusCode) else {
            throw NetworkError.invalidRespose
        }
        
        do{
            let decoder = JSONDecoder()
            return try decoder.decode(ReposModel.self, from: data)
        }catch{
           throw NetworkError.decodingError
        }
    }
}

enum NetworkError:Error{
    case invalidUrl
    case invalidRespose
    case decodingError
}

