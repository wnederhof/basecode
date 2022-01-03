export interface DataFormField {
  name: string
  label: string
}

export interface DataFormProps {
  fields: DataFormField[]
  value: any
}

export function DataDetails(props: DataFormProps) {
  return (
    <table>
      <tbody>
        {props.fields.map((field) => (
          <tr key={field.name}>
            <td>{field.label}</td>
            <td>{(props.value as any)[field.name]}</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
