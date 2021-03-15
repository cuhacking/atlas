import React, { useEffect, useState, useRef } from "react";
import styled from "styled-components";
import { com } from "Atlas-common";
import provideSearchViewModel = com.cuhacking.atlas.common.provideSearchViewModel;
import SearchResult = com.cuhacking.atlas.common.SearchResult;

const SearchInputField = styled.input`
  background: #ffffff;
`;

export const SearchBar = () => {
  const viewModel = useRef(provideSearchViewModel());
  const [results, setResults] = React.useState<SearchResult[]>([]);
  const [value, setValue] = React.useState<string>("");

  useEffect(() => {
    viewModel.current.searchResults.subscribe(
      (results) => {
        console.log(results);
      },
      () => {},
      () => {}
    );
  }, []);

  const handleValueChange = (newValue: string) => {
    setValue(newValue);
    viewModel.current.getSearchResults(newValue);
  };

  return (
    <SearchInputField
      value={value}
      onChange={(e) => {
        e.target.value;
      }}
      placeholder="Search..."
    />
  );
};
