import Link from 'next/link'

export interface DataListField {
  key: string
  label: string
}

export interface DataListProps<FieldType> {
  primaryKey: string
  fields: DataListField[]
  rows: FieldType[]
  canShow?: (row: FieldType) => boolean
  canEdit?: (row: FieldType) => boolean
  canDelete?: (row: FieldType) => boolean
  showUrl: (row: FieldType) => string
  onEdit: (row: FieldType) => void
  onDelete: (row: FieldType) => void
}

export function DataList<T>(props: DataListProps<T>) {
  return props.rows.length === 0 ? (
    <div>No entries.</div>
  ) : (
    <table>
      <thead>
        <tr>
          {props.fields.map((field) => (
            <th key={field.key}>{field.label}</th>
          ))}
          <th />
        </tr>
      </thead>
      <tbody>
        {props.rows.map((row: T) => (
          <tr key={(row as any)[props.primaryKey]}>
            {props.fields.map((field) => (
              <td key={field.key}>{(row as any)[field.key]}</td>
            ))}
            <td>
              {!props.canShow || props.canShow(row) ? (
                <Link href={props.showUrl(row)}>Show</Link>
              ) : (
                <></>
              )}
              <button
                disabled={props.canEdit && !props.canEdit(row)}
                onClick={() => props.onEdit(row)}
              >
                Edit
              </button>
              <button
                disabled={props.canDelete && !props.canDelete(row)}
                onClick={() => props.onDelete(row)}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
