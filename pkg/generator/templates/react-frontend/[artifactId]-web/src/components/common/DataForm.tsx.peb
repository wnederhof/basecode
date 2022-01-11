import { useState } from 'react'

export enum DataFormFieldType {
  RELATIONAL = 'RELATIONAL',
  RELATIONAL_OPTIONAL = 'RELATIONAL_OPTIONAL',
  TEXT = 'TEXT',
  TEXT_OPTIONAL = 'TEXT_OPTIONAL',
  INT = 'INT',
  INT_OPTIONAL = 'INT_OPTIONAL',
}

export interface DataFormFieldOption {
  label: string
  value: string
}

export interface DataFormField {
  name: string
  label: string
  options?: DataFormFieldOption[]
  type: DataFormFieldType
}

export interface DataFormProps {
  fields: DataFormField[]
  value: any
  onSubmit: (result: any) => void
  onCancel: () => void
}

const formElements = {
  RELATIONAL: (field: DataFormField, value: any, onUpdate: any) => (
    <select required>
      {field.options?.map((option) => (
        <option
          key={option.value}
          value={option.value}
          selected={option.value === value}
          onSelect={() => onUpdate(option.value)}
        >
          {option.label}
        </option>
      ))}
    </select>
  ),
  RELATIONAL_OPTIONAL: (field: DataFormField, value: any, onUpdate: any) => (
    <select>
      {field.options?.map((option) => (
        <option
          key={option.value}
          value={option.value}
          selected={option.value === value}
          onSelect={() => onUpdate(option.value)}
        >
          {option.label}
        </option>
      ))}
    </select>
  ),
  STRING: (field: DataFormField, value: any, onUpdate: any) => (
    <input
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
      required
    />
  ),
  STRING_OPTIONAL: (field: DataFormField, value: any, onUpdate: any) => (
    <input
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
    />
  ),
  TEXT: (field: DataFormField, value: any, onUpdate: any) => (
    <textarea
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
      required
    />
  ),
  TEXT_OPTIONAL: (field: DataFormField, value: any, onUpdate: any) => (
    <textarea
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
    />
  ),
  INT: (field: DataFormField, value: any, onUpdate: any) => (
    <input
      type="number"
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
      required
    />
  ),
  INT_OPTIONAL: (field: DataFormField, value: any, onUpdate: any) => (
    <input
      type="number"
      name={field.name}
      defaultValue={value}
      onChange={(e) => onUpdate(e.target.value)}
    />
  ),
}

export function DataForm(props: DataFormProps) {
  const [formData, setFormData] = useState(props.value)
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault()
        props.onSubmit(formData)
      }}
    >
      <table>
        <tbody>
          {props.fields.map((field) => (
            <tr key={field.name}>
              <td>{field.label}</td>
              <td>
                {formElements[field.type](
                  field,
                  formData[field.name],
                  (v: any) => {
                    const newValue = { ...formData, [field.name]: v }
                    setFormData(newValue)
                  }
                )}
              </td>
            </tr>
          ))}
        </tbody>
        <tr>
          <td />
          <td>
            <button>Submit</button>
            <button type="button" onClick={() => props.onCancel()}>
              Cancel
            </button>
          </td>
        </tr>
      </table>
    </form>
  )
}
