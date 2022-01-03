import { createContext, ReactNode, useContext } from 'react'

export interface ActionSectionProps {
  header?: string
  showBack: () => void
  onNew: () => void
  onEdit: () => void
  children: ReactNode
}

const LevelContext = createContext({ layoutLevel: 0 })

export function ActionSection(props: ActionSectionProps) {
  const layoutLevel = useContext(LevelContext)?.layoutLevel || 0
  let header: ReactNode
  switch (layoutLevel) {
    case 0:
      header = <h1>{props.header}</h1>
      break
    case 1:
      header = <h2>{props.header}</h2>
      break
    default:
      header = <h3>{props.header}</h3>
      break
  }
  return (
    <>
      {props.header ? header : <></>}
      {props.onNew ? (
        <button type="button" onClick={props.onNew}>
          New
        </button>
      ) : (
        <></>
      )}
      {props.onEdit ? (
        <button type="button" onClick={props.onEdit}>
          Edit
        </button>
      ) : (
        <></>
      )}
      <LevelContext.Provider value={{ layoutLevel: layoutLevel + 1 }}>
        {props.children}
      </LevelContext.Provider>
    </>
  )
}
