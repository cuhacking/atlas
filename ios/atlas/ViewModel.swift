//
//  ViewModel.swift
//  atlas
//
//  Created by Will Mahler on 2021-03-07.
//  Copyright © 2021 cuHacking. All rights reserved.
//

import Foundation
import Combine
import Common

class ViewModel : ObservableObject {

    @Published var searchText = ""

    let allData: [String]
    var filteredData: [String] = [String]()
    var publisher: AnyCancellable?

    init() {
        self.allData = (0..<30).map({ "\($0)" })
        self.filteredData = allData

        self.publisher = $searchText
            .receive(on: RunLoop.main)
            .sink(receiveValue: { (str) in
                if !self.searchText.isEmpty {
                    self.filteredData = self.allData.filter { $0.contains(str) }
                } else {
                    self.filteredData = []
                }
            })
    }
}
