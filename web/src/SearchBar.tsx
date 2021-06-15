import React, { useEffect, useState, useRef } from "react";
import styled from "styled-components";
import { com } from "Atlas-common";
import SearchResult = com.cuhacking.atlas.common.SearchResult;
import { searchViewModel } from ".";

const SearchInputField = styled.input`
  background: #ffffff;
  border: none;
  border-radius: 8px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23);
  padding: 8px;
  width: 256px;
  margin-bottom: 8px;

  &:focus {
    outline: none;
  }
`;

const SearchContainer = styled.div`
  margin: 16px;
`;

const SearchResultItem = styled.div`
  padding: 8px;
  height: 56px;
  background: var(--color-surface);
  display: flex;
  flex-direction: column;
  justify-content: center;
  cursor: pointer;
`;

const ResultTitle = styled.div``;
const ResultSubtitle = styled.div`
  font-size: 14px;
`;

export const SearchBar = () => {
  const [results, setResults] = React.useState<SearchResult[]>([]);
  const [value, setValue] = React.useState<string>("");

  useEffect(() => {
    searchViewModel.searchResults.subscribe(
      (results) => {
        setResults(results);
      },
      () => {},
      () => {}
    );
  }, []);

  const handleValueChange = (newValue: string) => {
    setValue(newValue);
    searchViewModel.getSearchResults(newValue);
  };

  return (
    <SearchContainer>
      <SearchInputField
        value={value}
        onChange={(e) => {
          handleValueChange(e.target.value);
        }}
        placeholder="Search..."
      />
      {results.map((result) => (
        <SearchResultItem>
          <ResultTitle>{result.name}</ResultTitle>
          <ResultSubtitle>{result.description}</ResultSubtitle>
        </SearchResultItem>
      ))}
    </SearchContainer>
  );
};
