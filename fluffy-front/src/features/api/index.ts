import axios, { AxiosResponse } from "axios";
import { useQuery } from "react-query";

const instance = axios.create({
  baseURL: 'http://localhost:8080'
})

export type OwnerResponse = {
  id: string,
  name: string
};

const fetchOwner = async (): Promise<OwnerResponse[]> => {
  const { data } = await instance.get<
    OwnerResponse[],
    AxiosResponse<OwnerResponse[]>
  >("/owners")
  return data
};

export const useOwner = () => {
  return useQuery({
    queryKey: ["owner-fetch"],
    queryFn: fetchOwner,
    staleTime: Infinity,
    cacheTime: Infinity,
  })
};
