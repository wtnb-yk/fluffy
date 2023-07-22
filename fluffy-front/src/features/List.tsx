import { OwnerResponse, useOwner } from "./api"

export default function List() {
  const { isLoading, isError, data } = useOwner()

  const renderList = (
    isLoading: boolean,
    isError: boolean,
    data: OwnerResponse[] | undefined
  ) => {
    if (isLoading) return <p>Loading...</p>
    if (isError) return <p>Error!!! Failed to fetch data</p>
    if (data === undefined) return <p>No data.</p>
    return data.map((item) => (
      <div key={item.id}>
        <span>{item.name}</span>
      </div>
    ))
  }

  return <div>
    {renderList(isLoading, isError, data)}
  </div>;
}
