import { PetResponse, useCreatePet, useDeletePet, useFetchPet } from "./api"
import { useForm, SubmitHandler } from "react-hook-form"
import * as yup from "yup"
import { yupResolver } from "@hookform/resolvers/yup"
import classes from "./List.module.scss"

export default function List() {
  const { isLoading, isError, data } = useFetchPet()

  const renderList = (
    isLoading: boolean,
    isError: boolean,
    data: PetResponse[] | undefined
  ) => {
    if (isLoading) return <p>Loading...</p>
    if (isError) return <p>Error!!! Failed to fetch data</p>
    if (data === undefined) return <p>No data.</p>
    return data.map((pet) => (
      <div key={pet.id} className={classes.Pet_Container}>
        <span className={classes.Pet_Name}>{pet.name}</span>
        <button onClick={() => deletePet(pet.id)}>delete</button>
      </div>
    ))
  }

  const PetFormSchema = yup.object({
    name: yup.string().required(),
  }).required()

  const { register, handleSubmit, reset } = useForm({
    resolver: yupResolver(PetFormSchema),
    defaultValues: { name: ''}
  });

  const updateMutation = useCreatePet();
  type PetFormSchema = yup.InferType<typeof PetFormSchema>;
  const submitPet: SubmitHandler<PetFormSchema> = (pet) => {
    updateMutation.mutate({name: pet.name}, {onSuccess: () => {reset()}});
  }

  const deleteMutation = useDeletePet();
  const deletePet = (id: string) => {
    deleteMutation.mutate(id, {onSuccess: () => {reset()}})
  }

  const renderAddPet = () => {
    if(updateMutation.isLoading) {
      return <p>Creating pet item...</p>
    } else {
      return <form onSubmit={handleSubmit(submitPet)}>
        <input {...register("name")} placeholder='name'/>
        <button type="submit">追加</button>
      </form>
    }
  }

  return <div>
    {renderList(isLoading, isError, data)}
    {renderAddPet()}
  </div>
}
