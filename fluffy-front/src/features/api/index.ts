import axios, { AxiosResponse } from "axios";
import { useQuery, useMutation, useQueryClient } from "react-query";

const instance = axios.create({
  baseURL: 'http://localhost:10280'
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

export const useFetchOwner = () => {
  return useQuery({
    queryKey: ["owner-fetch"],
    queryFn: fetchOwner,
    staleTime: Infinity,
    cacheTime: Infinity,
  })
};

export type OwnerAddRequest = {
  name: string
}

const createOwner = async (request: OwnerAddRequest): Promise<null> => {
  return await instance.post('/owners', request)
}

export const useCreateOwner = () => {
  const queryClient = useQueryClient()
  return useMutation(
    ['owner-create'],
    createOwner,
    {
      onSuccess: async () => {
        await queryClient.invalidateQueries({ queryKey: ['owner-fetch'] })
      }
    })
  }

  const deleteOwner = async (id: string): Promise<null> => {
    return await instance.delete('/owners/' + id)
  }
  
  export const useDeleteOwner = () => {
    const queryClient = useQueryClient()
    return useMutation(
      ['owner-delete'],
      deleteOwner,
      {
        onSuccess: async () => {
          await queryClient.invalidateQueries({ queryKey: ['owner-fetch'] })
        }
      }
    )
  }