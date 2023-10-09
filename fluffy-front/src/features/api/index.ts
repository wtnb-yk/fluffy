import axios, { AxiosResponse } from "axios";
import { useQuery, useMutation, useQueryClient } from "react-query";

const instance = axios.create({
  baseURL: 'http://localhost:10280/fluffy-api'
})

export type PetResponse = {
  id: string,
  name: string
};

const fetchPet = async (): Promise<PetResponse[]> => {
  const { data } = await instance.get<
    PetResponse[],
    AxiosResponse<PetResponse[]>
    // TODO: IDを仮置き
  >("/v1/pets/8211983c-b70d-4682-923a-ba11b475cdfa")
  return data
};

export const useFetchPet = () => {
  return useQuery({
    queryKey: ["pet-fetch"],
    queryFn: fetchPet,
    staleTime: Infinity,
    cacheTime: Infinity,
  })
};

export type PetAddRequest = {
  name: string
}

const createPet = async (request: PetAddRequest): Promise<null> => {
  return await instance.post('/pets', request)
}

export const useCreatePet = () => {
  const queryClient = useQueryClient()
  return useMutation(
    ['pet-create'],
    createPet,
    {
      onSuccess: async () => {
        await queryClient.invalidateQueries({ queryKey: ['pet-fetch'] })
      }
    })
  }

  const deletePet = async (id: string): Promise<null> => {
    return await instance.delete('/pets/' + id)
  }
  
  export const useDeletePet = () => {
    const queryClient = useQueryClient()
    return useMutation(
      ['pet-delete'],
      deletePet,
      {
        onSuccess: async () => {
          await queryClient.invalidateQueries({ queryKey: ['pet-fetch'] })
        }
      }
    )
  }