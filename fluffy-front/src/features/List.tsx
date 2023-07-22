import { OwnerResponse, useCreateOwner, useDeleteOwner, useFetchOwner } from "./api"
import { useForm, SubmitHandler } from "react-hook-form"
import * as yup from "yup"
import { yupResolver } from "@hookform/resolvers/yup"

export default function List() {
  const { isLoading, isError, data } = useFetchOwner()

  const renderList = (
    isLoading: boolean,
    isError: boolean,
    data: OwnerResponse[] | undefined
  ) => {
    if (isLoading) return <p>Loading...</p>
    if (isError) return <p>Error!!! Failed to fetch data</p>
    if (data === undefined) return <p>No data.</p>
    return data.map((owner) => (
      <div key={owner.id}>
        <span>{owner.name}</span>
        <button onClick={() => deleteOwner(owner.id)}>delete</button>
      </div>
    ))
  }

  const OwnerFormSchema = yup.object({
    name: yup.string().required(),
  }).required()

  const { register, handleSubmit, reset } = useForm({
    resolver: yupResolver(OwnerFormSchema),
    defaultValues: { name: ''}
  });

  const updateMutation = useCreateOwner();
  type OwnerFormSchema = yup.InferType<typeof OwnerFormSchema>;
  const submitOwner: SubmitHandler<OwnerFormSchema> = (owner) => {
    updateMutation.mutate({name: owner.name}, {onSuccess: () => {reset()}});
  }

  const deleteMutation = useDeleteOwner();
  const deleteOwner = (id: string) => {
    deleteMutation.mutate(id, {onSuccess: () => {reset()}})
  }

  const renderAddOwner = () => {
    if(updateMutation.isLoading) {
      return <p>Creating owner item...</p>
    } else {
      return <form onSubmit={handleSubmit(submitOwner)}>
        <input {...register("name")} placeholder='name'/>
        <button type="submit">追加</button>
      </form>
    }
  }

  return <div>
    {renderList(isLoading, isError, data)}
    {renderAddOwner()}
  </div>
}
